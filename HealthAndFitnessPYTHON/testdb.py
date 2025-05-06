from flask import Flask, jsonify, request
import requests
from transformers import AutoTokenizer, AutoModel
import torch
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from functools import lru_cache
from collections import defaultdict
from sklearn.metrics import ndcg_score
import random

app = Flask(__name__)

# --- GLOBAL TRAIN/TEST DATA ---
train_data = defaultdict(set)
test_data  = defaultdict(set)

# API URL'leri
RECIPES_URL   = "http://localhost:8080/v1/recipe"
LIKES_URL     = "http://localhost:8080/v1/like"
FAVORITES_URL = "http://localhost:8080/v1/favorite"

print("BERT modeli yükleniyor...")
tokenizer = AutoTokenizer.from_pretrained("dbmdz/bert-base-turkish-cased")
model     = AutoModel.from_pretrained("dbmdz/bert-base-turkish-cased")
print("Model yüklendi!")

def get_combined_text(recipe):
    ingredients = " ".join(
        f"{ing.get('quantity', '')} {ing.get('name', '')}".strip()
        for ing in recipe.get('ingredientList', [])
    )
    return f"{recipe.get('title', '')} {recipe.get('description', '')} {ingredients}".strip()

@lru_cache(maxsize=1000)
def get_embedding(text):
    if not text:
        return np.zeros(768)
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    last_hidden = outputs.last_hidden_state
    mask        = inputs['attention_mask'].unsqueeze(-1).expand(last_hidden.size()).float()
    summed      = torch.sum(last_hidden * mask, dim=1)
    counts      = torch.clamp(mask.sum(dim=1), min=1e-9)
    return (summed / counts).squeeze().numpy()

def get_batch_embeddings(texts):
    return [get_embedding(t) for t in texts] if texts else []

def fetch_data(url):
    try:
        r = requests.get(url)
        r.raise_for_status()
        return r.json()
    except Exception as e:
        print(f"API Error ({url}): {e}")
        return None

def filter_user_interactions(data, user_id):
    return [i for i in data if i.get('userId') == user_id] if data else []

def check_api_connections():
    apis = {"recipes": RECIPES_URL, "likes": LIKES_URL, "favorites": FAVORITES_URL}
    status = {}
    for name, url in apis.items():
        try:
            status[name] = requests.head(url, timeout=2).status_code == 200
        except:
            status[name] = False
    return status

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({
        "status": "healthy",
        "model_loaded": model is not None,
        "api_connections": check_api_connections()
    })

@app.route('/evaluate', methods=['GET'])
def evaluate_recommendation_system():
    global train_data, test_data

    all_recipes   = fetch_data(RECIPES_URL)
    all_likes     = fetch_data(LIKES_URL)
    all_favorites = fetch_data(FAVORITES_URL)
    if any(x is None for x in (all_recipes, all_likes, all_favorites)):
        return jsonify({"error": "Veriler alınamadı"}), 500

    user_interactions = defaultdict(set)
    for e in all_likes + all_favorites:
        user_interactions[e["userId"]].add(e["recipeId"])

    train_data.clear()
    test_data.clear()
    for user, items in user_interactions.items():
        if len(items) < 3:
            continue
        items = list(items)
        random.shuffle(items)
        split = int(0.8 * len(items))
        train_data[user] = set(items[:split])
        test_data[user]  = set(items[split:])

    def precision_at_k(pred, actual, k=10):
        return len(set(pred[:k]) & actual) / k if pred else 0.0

    def recall_at_k(pred, actual, k=10):
        return len(set(pred[:k]) & actual) / len(actual) if actual else 0.0

    def ndcg_at_k(pred, actual, k=10):
        y_true  = [[1 if pid in actual else 0 for pid in pred[:k]]]
        y_score = [[k - i for i in range(len(pred[:k]))]]
        return ndcg_score(y_true, y_score)

    precision_list, recall_list, ndcg_list = [], [], []
    for user_id, actual in test_data.items():
        resp = requests.get(f"http://localhost:5000/recommendations/{user_id}")
        if resp.status_code != 200:
            continue
        predicted = [r["id"] for r in resp.json()]
        if not predicted or not actual:
            continue
        precision_list.append(precision_at_k(predicted, actual))
        recall_list.append(recall_at_k(predicted, actual))
        ndcg_list.append(ndcg_at_k(predicted, actual))

    mean = lambda xs: round(sum(xs)/len(xs), 4) if xs else 0.0
    return jsonify({
        "tested_users": len(precision_list),
        "precision@10": mean(precision_list),
        "recall@10":    mean(recall_list),
        "ndcg@10":      mean(ndcg_list)
    })

@app.route('/recommendations/<int:user_id>', methods=['GET'])
def get_recommendations(user_id):
    max_prep_time = request.args.get('max_prep_time', type=int)
    max_calories  = request.args.get('max_calories', type=int)
    diversity     = float(request.args.get('diversity', 0.3))

    all_recipes   = fetch_data(RECIPES_URL)
    all_likes     = fetch_data(LIKES_URL)
    all_favorites = fetch_data(FAVORITES_URL)
    if any(x is None for x in (all_recipes, all_likes, all_favorites)):
        return jsonify({"error": "Temel veriler alınamadı"}), 500

    user_likes     = filter_user_interactions(all_likes, user_id)
    user_favorites = filter_user_interactions(all_favorites, user_id)
    liked_ids      = {l['recipeId'] for l in user_likes}
    favorited_ids  = {f['recipeId'] for f in user_favorites}

    interactions = {
        rid: (2.5 if rid in liked_ids and rid in favorited_ids
              else 1.5 if rid in favorited_ids
              else 1.2)
        for rid in (liked_ids | favorited_ids)
    }

    recipes = {r['id']: r for r in all_recipes}
    if max_prep_time:
        recipes = {i:r for i,r in recipes.items() if r.get('prepTime', float('inf')) <= max_prep_time}
    if max_calories:
        recipes = {i:r for i,r in recipes.items() if r.get('calories', float('inf')) <= max_calories}

    train_items = train_data.get(user_id, set())

    weighted_embs = []
    for rid, w in interactions.items():
        if rid in recipes:
            weighted_embs.append(get_embedding(get_combined_text(recipes[rid])) * w)
    if not weighted_embs:
        return jsonify({"message": "Yeterli veri yok"}), 400

    recipe_ids   = list(recipes.keys())
    embeddings   = get_batch_embeddings([get_combined_text(recipes[i]) for i in recipe_ids])
    user_profile = np.mean(weighted_embs, axis=0)
    sims         = cosine_similarity([user_profile], np.array(embeddings))[0]

    results = []
    for idx, rid in enumerate(recipe_ids):
        # Burada hem eğitim hem de kullanıcıya ait like/favorite filtresi:
        if rid in train_items or rid in liked_ids or rid in favorited_ids:
            continue
        r = recipes[rid]
        results.append({
            "id":             rid,
            "title":          r.get('title'),
            "description":    r.get('description'),
            "prepTime":       r.get('prepTime'),
            "calories":       r.get('calories'),
            "createdAt":      r.get('createdAt'),
            "likeCount":      r.get('likeCount'),
            "userId":         r.get('userId'),
            "score":          float(sims[idx]),
            "imageUrl":       r.get('imageUrl')
        })

    results.sort(key=lambda x: x['score'], reverse=True)
    if diversity > 0:
        top5 = results[:5]
        rest = results[5:30]
        random.seed(user_id)
        random.shuffle(rest)
        return jsonify(top5 + rest[:15])
    return jsonify(results[:20])

@app.route('/similar-recipes/<int:recipe_id>', methods=['GET'])
def get_similar_recipes(recipe_id):
    limit = request.args.get('limit', default=10, type=int)
    all_recipes = fetch_data(RECIPES_URL)
    if all_recipes is None:
        return jsonify({"error": "Tarifler alınamadı"}), 500
    recipes = {r['id']: r for r in all_recipes}
    if recipe_id not in recipes:
        return jsonify({"error": "Tarif bulunamadı"}), 404

    tgt_emb = get_embedding(get_combined_text(recipes[recipe_id]))
    others, ids = [], []
    for rid, r in recipes.items():
        if rid == recipe_id: continue
        ids.append(rid)
        others.append(get_embedding(get_combined_text(r)))

    sims = cosine_similarity([tgt_emb], others)[0]
    results = [{
        "id":              rid,
        "title":           recipes[rid]['title'],
        "description":     recipes[rid]['description'],
        "prepTime":        recipes[rid]['prepTime'],
        "calories":        recipes[rid]['calories'],
        "imageUrl":        recipes[rid]['imageUrl'],
        "similarity_score": float(sims[i])
    } for i, rid in enumerate(ids)]
    results.sort(key=lambda x: x['similarity_score'], reverse=True)
    return jsonify(results[:limit])

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
