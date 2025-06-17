# 🧠 Sağlık ve Fitness Platformu

Kapsamlı bir **dijital sağlık, fitness ve beslenme** platformu. Kullanıcıların bireysel sağlık bilgilerine göre özel öneriler alabileceği, yemek tariflerini keşfedip paylaşabileceği, fiziksel aktivite ve vücut analizlerini takip edebileceği sosyal bir ortam sunar.

## 🚀 Proje Özeti

Bu platform;

- 🥗 **Yemek tarifleri** paylaşımı ve öneri sistemi,
- 🧘 **Aktivite takibi** (örneğin egzersiz süresi, adım sayısı),
- 📊 **Kişisel sağlık analizi** (BMI, kalorimetre vb.),
- 🧑‍🤝‍🧑 **Sosyal medya benzeri etkileşim** (yorum, beğeni, takip),
- 🤖 **Python + BERT tabanlı öneri sistemi** ile kullanıcıya özel tarif önerileri sunar.

## 📚 Teknolojiler

### 🎯 Frontend
- **Angular** 17+
- Tailwind CSS
- RxJS & Angular Forms
- JWT Authentication (token bazlı)

### 🛠️ Backend
- **Spring Boot** (Java)
  - RESTful API
  - JWT Security
  - MySQL / PostgreSQL
  - Redis (opsiyonel cache)
  - Dockerized yapı

### 🧠 Yapay Zekâ / Öneri Sistemi
- **Python + Flask API**
- BERT tabanlı cümle embedding
- Tarif benzerliği üzerinden öneri sistemi
- Kullanıcının favorileri ve geçmişine göre kişiselleştirme

## 🧪 Özellikler

| Özellik | Açıklama |
|--------|----------|
| 👤 Kullanıcı Profili | Kayıt, giriş, profil bilgileri, takip sistemi |
| 🍽️ Tarif Paylaşımı | Başlık, içerik, fotoğraf, kategori, malzeme bilgileri |
| 📈 Analizler | Kullanıcının boy, kilo verisine göre BMI hesabı |
| 💬 Sosyal Etkileşim | Beğeni, yorum, takip ve bildirim sistemi |
| 🧬 Öneri Sistemi | Favorilere göre benzer tarifler önerilir (Python Flask) |
| 🗂️ Admin Paneli | Rol bazlı yetkilendirme ve içerik kontrolü |

## 📸 Ekran Görüntüleri (Opsiyonel)
> Eğer görsellerin varsa buraya ekle:
\`\`\`bash
src/assets/screens/homepage.png
src/assets/screens/profile-page.png
src/assets/screens/suggestion-system.png
\`\`\`

## ⚙️ Kurulum

### Backend (Spring Boot)

\`\`\`bash
cd server
./mvnw clean install
java -jar target/fitness-platform.jar
\`\`\`

### Frontend (Angular)

\`\`\`bash
cd client
npm install
ng serve --open
\`\`\`

### Öneri Servisi (Python Flask)

\`\`\`bash
cd recommendation-service
pip install -r requirements.txt
python app.py
\`\`\`

## 🧪 API Dökümantasyonu

Swagger UI ile tüm API'ler test edilebilir:

\`\`\`
http://localhost:8080/swagger-ui/index.html
\`\`\`

## 🗃️ Veritabanı Tasarımı (Özet)

- \`User\`: id, name, email, height, weight, profile_photo
- \`Recipe\`: id, title, description, ingredients, category, user_id
- \`Activity\`: id, duration, type, calories_burned, user_id
- \`Favorite\`: user_id, recipe_id
- \`Comment\`, \`Like\`, \`Follow\` tabloları

## 🔐 Güvenlik

- Spring Security + JWT
- Role-based erişim kontrolü (USER / ADMIN)
- Giriş / kayıt endpoint’leri public, diğerleri auth gerektiriyor

## 📦 Docker Desteği

\`\`\`bash
docker-compose up --build
\`\`\`

> Frontend, backend ve öneri sistemi için örnek \`docker-compose.yml\` dosyası mevcuttur.

## ✍️ Katkı Sağlayanlar

- 👨‍💻 Ali Turgut - Geliştirici & Araştırmacı
- 🤖 BERT + Flask Öneri Sistemi (ML Modülü)

## 📄 Lisans

MIT Lisansı © 2025 Ali Turgut
