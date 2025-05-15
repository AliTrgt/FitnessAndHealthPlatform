from flask import Flask, jsonify
import requests
from sentence_transformers import SentenceTransformer
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)

# API URL'leri
RECIPES_URL = "http://localhost:8080/v1/recipe"
LIKES_URL = "http://localhost:8080/v1/like"
FAVORITES_URL = "http://localhost:8080/v1/favorite"

# Model yükleniyor (daha etkili, çok dilli model)
model = SentenceTransformer('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2')

def get_combined_text(recipe):
    """title, description ve ingredientList alanlarını birleştirir"""
    title = recipe.get('title', '')
    description = recipe.get('description', '')
    ingredients = " ".join(recipe.get('ingredientList', []))
    return f"{title} {description} {ingredients}".strip()

def get_embedding(text):
    """Metni embedding'e dönüştürür"""
    if not text:
        return np.zeros(384)  # Bu model 384 boyutlu
    return model.encode(text)

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

@app.route('/recommendations/<int:user_id>', methods=['GET'])
def get_recommendations(user_id):
    # Verileri çek
    all_recipes = fetch_data(RECIPES_URL)
    all_likes = fetch_data(LIKES_URL)
    all_favorites = fetch_data(FAVORITES_URL)

    if not all([all_recipes, all_likes is not None, all_favorites is not None]):
        return jsonify({"error": "Temel veriler alınamadı"}), 500

    user_likes = filter_user_interactions(all_likes, user_id)
    user_favorites = filter_user_interactions(all_favorites, user_id)

    liked_ids = [int(like['recipeId']) for like in user_likes]
    favorited_ids = [int(fav['recipeId']) for fav in user_favorites]

    # Ağırlıklı kullanıcı etkileşimleri
    interactions = {}
    for rid in set(liked_ids + favorited_ids):
        interactions[int(rid)] = 2.0 if (rid in liked_ids and rid in favorited_ids) else 1.5 if rid in favorited_ids else 1.2

    recipes = {int(r['id']): r for r in all_recipes}

    # Kullanıcı profil embedding'i
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
        return jsonify({"message": "Yeterli etkileşim verisi yok"}), 400

    # Tüm tariflerin embedding'leri
    recipe_ids = list(recipes.keys())
    recipe_texts = [get_combined_text(recipes[rid]) for rid in recipe_ids]
    recipe_embeddings = np.array([get_embedding(text) for text in recipe_texts])

    # Ortalama kullanıcı profili
    user_profile = np.mean(weighted_embeddings, axis=0)

    # Benzerlik hesapla
    similarities = cosine_similarity([user_profile], recipe_embeddings)[0]

    # Önerileri topla
    results = []
    for idx, rid in enumerate(recipe_ids):
        if rid not in interactions and similarities[idx] > 0.5:  # 0.5 eşik değeri
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
                "ingredientList": recipe.get('ingredientList', []),
                "likeList": recipe.get('likeList', []),
                "commentList": recipe.get('commentList', []),
                "score": float(similarities[idx]),
                "imageUrl": recipe.get('imageUrl')
            })

    results.sort(key=lambda x: x['score'], reverse=True)
    return jsonify(results[:20])

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
