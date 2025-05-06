from flask import Flask, jsonify
import requests
from transformers import AutoTokenizer, AutoModel
import torch
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from functools import lru_cache
from collections import defaultdict
from sklearn.model_selection import train_test_split

app = Flask(__name__)

RECIPES_URL = "http://localhost:8080/v1/recipe"
LIKES_URL = "http://localhost:8080/v1/like"
FAVORITES_URL = "http://localhost:8080/v1/favorite"

tokenizer = AutoTokenizer.from_pretrained("dbmdz/bert-base-turkish-cased")
model = AutoModel.from_pretrained("dbmdz/bert-base-turkish-cased")

def get_combined_text(recipe):
    ingredients = " ".join([f"{ing.get('quantity', '')} {ing.get('name', '')}".strip() for ing in recipe.get('ingredientList', [])])
    return f"{recipe.get('title', '')} {recipe.get('description', '')} {ingredients}".strip()

@lru_cache(maxsize=1000)
def get_embedding(text):
    if not text:
        return np.zeros(768)
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    last_hidden = outputs.last_hidden_state
    attention_mask = inputs['attention_mask']
    mask_expanded = attention_mask.unsqueeze(-1).expand(last_hidden.size()).float()
    sum_embeddings = torch.sum(last_hidden * mask_expanded, dim=1)
    sum_mask = torch.clamp(mask_expanded.sum(dim=1), min=1e-9)
    return (sum_embeddings / sum_mask).squeeze().numpy()

def fetch_data(url):
    try:
        response = requests.get(url)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException:
        return None

@app.route('/health', methods=['GET'])
def health_check():
    status = {
        "status": "healthy",
        "model_loaded": True,
        "api_connections": {
            "recipes": requests.head(RECIPES_URL).status_code == 200,
            "likes": requests.head(LIKES_URL).status_code == 200,
            "favorites": requests.head(FAVORITES_URL).status_code == 200
        }
    }
    return jsonify(status)

@app.route('/recommendations/<int:user_id>', methods=['GET'])
def get_recommendations(user_id):
    all_recipes = fetch_data(RECIPES_URL)
    all_likes = fetch_data(LIKES_URL)
    all_favorites = fetch_data(FAVORITES_URL)

    if not all([all_recipes, all_likes, all_favorites]):
        return jsonify({"error": "Veri alınamadı"}), 500

    user_interactions = {
        "likes": [like['recipeId'] for like in all_likes if like['userId'] == user_id],
        "favorites": [fav['recipeId'] for fav in all_favorites if fav['userId'] == user_id]
    }

    recipes = {r['id']: r for r in all_recipes}
    embeddings = []
    
    for rid in set(user_interactions["likes"] + user_interactions["favorites"]):
        if rid in recipes:
            text = get_combined_text(recipes[rid])
            weight = 2.0 if (rid in user_interactions["likes"] and rid in user_interactions["favorites"]) else 1.5 if rid in user_interactions["favorites"] else 1.2
            embeddings.append(get_embedding(text) * weight)

    if not embeddings:
        return jsonify({"message": "Yetersiz veri"}), 400

    user_profile = np.mean(embeddings, axis=0)
    all_embeddings = [get_embedding(get_combined_text(r)) for r in all_recipes]
    similarities = cosine_similarity([user_profile], all_embeddings)[0]
    
    results = []
    for idx, recipe in enumerate(all_recipes):
        if recipe['id'] not in user_interactions["likes"] + user_interactions["favorites"]:
            results.append({
                "id": recipe['id'],
                "score": float(similarities[idx]),
                **{k: v for k, v in recipe.items() if k != 'ingredientList'}
            })
    
    results.sort(key=lambda x: x['score'], reverse=True)
    return jsonify(results[:20])

@app.route('/evaluate', methods=['GET'])
def evaluate_metrics():
    K = 10
    TEST_SIZE = 0.2
    all_recipes = fetch_data(RECIPES_URL)
    all_likes = fetch_data(LIKES_URL)
    all_favorites = fetch_data(FAVORITES_URL)

    if not all([all_recipes, all_likes, all_favorites]):
        return jsonify({"error": "Veri alınamadı"}), 500

    recipes = {r['id']: r for r in all_recipes}
    user_interactions = defaultdict(dict)

    for like in all_likes:
        user_id = like['userId']
        rid = like['recipeId']
        user_interactions[user_id].setdefault(rid, {'like': False, 'favorite': False})
        user_interactions[user_id][rid]['like'] = True

    for fav in all_favorites:
        user_id = fav['userId']
        rid = fav['recipeId']
        user_interactions[user_id].setdefault(rid, {'like': False, 'favorite': False})
        user_interactions[user_id][rid]['favorite'] = True

    total_precision = 0.0
    total_recall = 0.0
    total_ndcg = 0.0
    num_users = 0

    for user_id, interactions in user_interactions.items():
        if len(interactions) < 2:
            continue

        rids = list(interactions.keys())
        try:
            train_rids, test_rids = train_test_split(rids, test_size=TEST_SIZE, random_state=42)
        except ValueError:
            continue

        if not test_rids:
            continue

        embeddings = []
        for rid in train_rids:
            weight = 2.0 if interactions[rid]['like'] and interactions[rid]['favorite'] else 1.5 if interactions[rid]['favorite'] else 1.2
            text = get_combined_text(recipes[rid])
            embeddings.append(get_embedding(text) * weight)

        if not embeddings:
            continue

        user_profile = np.mean(embeddings, axis=0)
        candidate_rids = [rid for rid in recipes.keys() if rid not in train_rids]
        candidate_embeddings = [get_embedding(get_combined_text(recipes[rid])) for rid in candidate_rids]

        if not candidate_embeddings:
            continue

        similarities = cosine_similarity([user_profile], candidate_embeddings)[0]
        top_k_indices = np.argsort(similarities)[::-1][:K]
        top_k_rids = [candidate_rids[i] for i in top_k_indices]

        hits = sum(1 for rid in top_k_rids if rid in test_rids)
        precision = hits / K
        recall = hits / len(test_rids) if test_rids else 0

        dcg = sum(1 / np.log2(i + 2) for i, rid in enumerate(top_k_rids) if rid in test_rids)
        idcg = sum(1 / np.log2(i + 2) for i in range(min(len(test_rids), K)))
        ndcg = dcg / idcg if idcg > 0 else 0

        total_precision += precision
        total_recall += recall
        total_ndcg += ndcg
        num_users += 1

    if num_users == 0:
        return jsonify({"error": "Değerlendirilebilir kullanıcı yok"}), 400

    return jsonify({
        "parameters": {"k": K, "test_size": TEST_SIZE},
        "metrics": {
            "precision@k": total_precision / num_users,
            "recall@k": total_recall / num_users,
            "ndcg@k": total_ndcg / num_users
        },
        "evaluated_users": num_users
    })

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)