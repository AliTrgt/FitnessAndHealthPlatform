from flask import Flask, jsonify
import requests
from transformers import AutoTokenizer, AutoModel
import torch
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)

# API URL'leri
RECIPES_URL = "http://localhost:8080/v1/recipe"
LIKES_URL = "http://localhost:8080/v1/like"
FAVORITES_URL = "http://localhost:8080/v1/favorite"

# BERT Tokenizer ve Model
tokenizer = AutoTokenizer.from_pretrained("dbmdz/bert-base-turkish-cased")
model = AutoModel.from_pretrained("dbmdz/bert-base-turkish-cased")

def get_combined_text(recipe):
    """title, description ve ingredient list'i birleştirir"""
    title = safe_text(recipe.get('title', ''))
    description = safe_text(recipe.get('description', ''))
    ingredients = safe_text(recipe.get('ingredientList', []))  # Malzeme listesi
    return f"{title} {description} {ingredients}".strip()

def safe_text(text):
    """Boş metinleri kontrol eder"""
    if isinstance(text, list):
        text = " ".join(text)  # Ingredient listesi bir liste olabilir, onu stringe çevir
    return text if text else ""

def get_embedding(text):
    """Metni BERT embedding'ine dönüştürür"""
    if not text:
        return np.zeros(768)  # Boş metin için fallback
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    return outputs.last_hidden_state[:, 0, :].squeeze().numpy()

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

def contains_meat(ingredients):
    """Et içeriklerini kontrol eder"""
    meat_keywords = ['tavuk', 'kıyma', 'et', 'biftek', 'balık', 'köfte']
    return any(meat in ingredients.lower() for meat in meat_keywords)

@app.route('/recommendations/<int:user_id>', methods=['GET'])
def get_recommendations(user_id):
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

    weighted_embeddings = []
    for rid, weight in interactions.items():
        if rid in recipes:
            try:
                combined_text = get_combined_text(recipes[rid])
                embedding = get_embedding(combined_text) * weight
                weighted_embeddings.append((rid, embedding))
            except Exception as e:
                print(f"Embedding hatası - Recipe {rid}: {str(e)}")

    if not weighted_embeddings:
        return jsonify({"message": "Yeterli veri yok"}), 400

    # Tüm tariflerin embedding'leri
    recipe_embeddings = []
    for rid, recipe in recipes.items():
        try:
            combined_text = get_combined_text(recipe)
            recipe_embeddings.append((rid, get_embedding(combined_text)))
        except Exception as e:
            print(f"Tarif {rid} embedding hatası: {str(e)}")
            recipe_embeddings.append((rid, np.zeros(768)))  # Fallback
    
    # Etli tarifleri öne çıkar
    meat_recipes = [r for r in recipes.values() if contains_meat(r.get('ingredientList', []))]
    non_meat_recipes = [r for r in recipes.values() if not contains_meat(r.get('ingredientList', []))]

    # Benzerlik hesapla
    user_profile = np.mean([embedding for _, embedding in weighted_embeddings], axis=0)
    similarities = cosine_similarity([user_profile], np.array([embedding for _, embedding in recipe_embeddings]))[0]

    # Etli tarifleri sıralayıp en üstte tut
    meat_results = []
    for idx, (rid, recipe) in enumerate(meat_recipes):
        meat_results.append({
            "id": rid,
            "title": recipe.get('title'),
            "description": recipe.get('description'),
            "prepTime": recipe.get('prepTime'),
            "calories": recipe.get('calories'),
            "createdAt": recipe.get('createdAt'),
            "likeCount": recipe.get('likeCount'),
            "userId": recipe.get('userId'),
            "ingredientList": recipe.get('ingredientList', []),
            "likeList": recipe.get('likeList', []),
            "commentList": recipe.get('commentList', []),
            "score": float(similarities[idx]),
            "imageUrl": recipe.get('imageUrl')
        })

    # Et içermeyen tarifleri ekle
    non_meat_results = []
    for idx, (rid, recipe) in enumerate(non_meat_recipes):
        non_meat_results.append({
            "id": rid,
            "title": recipe.get('title'),
            "description": recipe.get('description'),
            "prepTime": recipe.get('prepTime'),
            "calories": recipe.get('calories'),
            "createdAt": recipe.get('createdAt'),
            "likeCount": recipe.get('likeCount'),
            "userId": recipe.get('userId'),
            "ingredientList": recipe.get('ingredientList', []),
            "likeList": recipe.get('likeList', []),
            "commentList": recipe.get('commentList', []),
            "score": float(similarities[idx]),
            "imageUrl": recipe.get('imageUrl')
        })

    # Sıralama
    meat_results.sort(key=lambda x: x['score'], reverse=True)
    non_meat_results.sort(key=lambda x: x['score'], reverse=True)

    # Sonuçları birleştir
    results = meat_results + non_meat_results

    return jsonify(results[:20])

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
