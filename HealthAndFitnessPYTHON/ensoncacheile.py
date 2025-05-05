from flask import Flask, jsonify, request
import requests
from transformers import AutoTokenizer, AutoModel
import torch
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from functools import lru_cache

app = Flask(__name__)

# API URL'leri
RECIPES_URL = "http://localhost:8080/v1/recipe"
LIKES_URL = "http://localhost:8080/v1/like"
FAVORITES_URL = "http://localhost:8080/v1/favorite"

print("BERT modeli yükleniyor...")
# BERT Tokenizer ve Model
tokenizer = AutoTokenizer.from_pretrained("dbmdz/bert-base-turkish-cased")
model = AutoModel.from_pretrained("dbmdz/bert-base-turkish-cased")
print("Model yüklendi!")

def get_combined_text(recipe):
    """title, description ve ingredient (quantity + name) alanlarını birleştirir"""
    ingredients = " ".join([
        f"{ing.get('quantity', '')} {ing.get('name', '')}".strip()
        for ing in recipe.get('ingredientList', [])
    ])
    return f"{recipe.get('title', '')} {recipe.get('description', '')} {ingredients}".strip()

@lru_cache(maxsize=1000)
def get_embedding(text):
    """Metni BERT embedding'ine dönüştürür (mean pooling ile + önbellekleme)"""
    if not text:
        return np.zeros(768)
    
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)

    # Mean pooling
    last_hidden = outputs.last_hidden_state
    attention_mask = inputs['attention_mask']
    mask_expanded = attention_mask.unsqueeze(-1).expand(last_hidden.size()).float()
    sum_embeddings = torch.sum(last_hidden * mask_expanded, dim=1)
    sum_mask = torch.clamp(mask_expanded.sum(dim=1), min=1e-9)
    
    # NumPy array döndürmeden önce has hesaplayalım
    embedding_array = (sum_embeddings / sum_mask).squeeze().numpy()
    return embedding_array

def get_batch_embeddings(texts):
    """Birden fazla metni tek seferde işler"""
    if not texts:
        return []
    
    # Her metin için önbellekten kontrol et
    embeddings = []
    for text in texts:
        embeddings.append(get_embedding(text))
    
    return embeddings

def fetch_data(url):
    """API'den veri çeker"""
    try:
        response = requests.get(url)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"API Error ({url}): {str(e)}")
        return None

def filter_user_interactions(data, user_id):
    """Kullanıcı etkileşimlerini filtreler"""
    return [item for item in data if item.get('userId') == user_id] if data else []

def check_api_connections():
    """API bağlantılarını kontrol eder"""
    apis = {
        "recipes": RECIPES_URL,
        "likes": LIKES_URL,
        "favorites": FAVORITES_URL
    }
    
    status = {}
    for name, url in apis.items():
        try:
            response = requests.head(url, timeout=2)
            status[name] = response.status_code == 200
        except:
            status[name] = False
    
    return status

@app.route('/health', methods=['GET'])
def health_check():
    """Sistem sağlık durumu kontrolü"""
    health_status = {
        "status": "healthy",
        "model_loaded": model is not None,
        "api_connections": check_api_connections()
    }
    return jsonify(health_status)

@app.route('/recommendations/<int:user_id>', methods=['GET'])
def get_recommendations(user_id):
    # URL parametrelerini al
    max_prep_time = request.args.get('max_prep_time', type=int)
    max_calories = request.args.get('max_calories', type=int)
    diversity_factor = float(request.args.get('diversity', 0.3))
    
    # Verileri çek
    all_recipes = fetch_data(RECIPES_URL)
    all_likes = fetch_data(LIKES_URL)
    all_favorites = fetch_data(FAVORITES_URL)

    # Hata kontrolü
    if not all([all_recipes, all_likes is not None, all_favorites is not None]):
        return jsonify({"error": "Temel veriler alınamadı"}), 500

    user_likes = filter_user_interactions(all_likes, user_id)
    user_favorites = filter_user_interactions(all_favorites, user_id)
    
    liked_ids = [like['recipeId'] for like in user_likes]
    favorited_ids = [fav['recipeId'] for fav in user_favorites]
    
    # Ağırlıklandırma
    interactions = {}
    for rid in set(liked_ids + favorited_ids):
        interactions[rid] = 2.0 if (rid in liked_ids and rid in favorited_ids) else 1.5 if rid in favorited_ids else 1.2

    # Tarif sözlüğü
    recipes = {r['id']: r for r in all_recipes}

    # Filtrele (eğer parametreler sağlanmışsa)
    if max_prep_time:
        recipes = {k: v for k, v in recipes.items() if v.get('prepTime', float('inf')) <= max_prep_time}
    
    if max_calories:
        recipes = {k: v for k, v in recipes.items() if v.get('calories', float('inf')) <= max_calories}

    weighted_embeddings = []
    for rid, weight in interactions.items():
        if rid in recipes:
            try:
                combined_text = get_combined_text(recipes[rid])
                embedding = get_embedding(combined_text) * weight
                weighted_embeddings.append(embedding)
            except Exception as e:
                print(f"Embedding hatası - Recipe {rid}: {str(e)}")

    if not weighted_embeddings:
        return jsonify({"message": "Yeterli veri yok"}), 400

    # Tüm tariflerin embedding'leri - kombinasyonları önceden hazırla
    recipe_texts = []
    recipe_ids = []
    
    for rid, r in recipes.items():
        recipe_ids.append(rid)
        recipe_texts.append(get_combined_text(r))
    
    # Batch olarak embeddingler hesaplanıyor
    recipe_embeddings = get_batch_embeddings(recipe_texts)
    
    # Benzerlik hesapla
    user_profile = np.mean(weighted_embeddings, axis=0)
    similarities = cosine_similarity([user_profile], np.array(recipe_embeddings))[0]

    # Sonuçları hazırla
    results = []
    for idx, rid in enumerate(recipe_ids):
        if rid not in interactions:  # Kullanıcının etkileşimde bulunmadığı tarifler
            recipe = recipes[rid]
            results.append({
                "id": rid,
                "title": recipe.get('title'),
                "description": recipe.get('description'),
                "prepTime": recipe.get('prepTime'),
                "calories": recipe.get('calories'),
                "createdAt": recipe.get('createdAt'),
                "likeCount": recipe.get('likeCount'),
                "userId": recipe.get('userId'),
                "score": float(similarities[idx]),
                "imageUrl": recipe.get('imageUrl')
            })

    # Sırala
    results.sort(key=lambda x: x['score'], reverse=True)
    
    # Çeşitlilik faktörüne göre sonuçları karıştır
    if diversity_factor > 0:
        top_results = results[:5]  # En üst 5 sonuç her zaman korunur
        rest_results = results[5:30]  # Sonraki sonuçlar
        
        # Çeşitlilik için sonuçları biraz karıştır
        import random
        random.seed(user_id)  # Tutarlı sonuçlar için
        random.shuffle(rest_results)
        
        # Karıştırılmış sonuçları birleştir
        final_results = top_results + rest_results[:15]
        return jsonify(final_results)
    
    # Normal sonuçlar (çeşitlilik olmadan)
    return jsonify(results[:20])

@app.route('/similar-recipes/<int:recipe_id>', methods=['GET'])
def get_similar_recipes(recipe_id):
    """Belirli bir tarife benzer tarifleri bulur"""
    limit = request.args.get('limit', default=10, type=int)
    
    # Tüm tarifleri çek
    all_recipes = fetch_data(RECIPES_URL)
    if not all_recipes:
        return jsonify({"error": "Tarifler alınamadı"}), 500
    
    # Tarif sözlüğü
    recipes = {r['id']: r for r in all_recipes}
    
    # İstenen tarif mevcut mu?
    if recipe_id not in recipes:
        return jsonify({"error": "Tarif bulunamadı"}), 404
    
    # Hedef tarifin gömülümünü al
    target_recipe = recipes[recipe_id]
    target_text = get_combined_text(target_recipe)
    target_embedding = get_embedding(target_text)
    
    # Diğer tariflerin gömülümlerini hesapla
    other_embeddings = []
    other_recipe_ids = []
    
    for rid, recipe in recipes.items():
        if rid != recipe_id:  # Hedef tarifin kendisini hariç tut
            other_recipe_ids.append(rid)
            combined_text = get_combined_text(recipe)
            other_embeddings.append(get_embedding(combined_text))
    
    # Benzerlik hesapla
    similarities = cosine_similarity([target_embedding], other_embeddings)[0]
    
    # Sonuçları hazırla
    results = []
    for idx, rid in enumerate(other_recipe_ids):
        recipe = recipes[rid]
        results.append({
            "id": rid,
            "title": recipe.get('title'),
            "description": recipe.get('description'),
            "prepTime": recipe.get('prepTime'),
            "calories": recipe.get('calories'),
            "imageUrl": recipe.get('imageUrl'),
            "similarity_score": float(similarities[idx])
        })
    
    # Sırala ve dön
    results.sort(key=lambda x: x['similarity_score'], reverse=True)
    return jsonify(results[:limit])

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)