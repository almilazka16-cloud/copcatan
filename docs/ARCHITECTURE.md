# Backend & AI Mimarisi Önerisi

Bu doküman, mühendis arkadaşınla konuşurken elinizde somut bir başlangıç
noktası olması için hazırlandı. **Bir öneridir, kesin karar değil** — teknik
kararı en iyi o verebilir. Amaç: tartışmayı sıfırdan değil, bir taslak
üzerinden başlatmak.

## Genel Yaklaşım: Firebase ile Başla, Gerekirse Göç Et

**Öneri:** MVP için Firebase (Auth + Firestore + Cloud Functions), AI için
Claude API.

**Neden:**
- Tek mühendis + kod bilmeyen bir kurucu var. Sunucu yönetimi, deployment,
  ölçekleme gibi işlerle uğraşmadan hızlı ilerlemeyi sağlar.
- Native Android ile SDK entegrasyonu çok olgun ve iyi dokümante.
- Gerçek zamanlı mesajlaşma (Firestore listeners) neredeyse hazır geliyor,
  sıfırdan WebSocket altyapısı kurmaya gerek kalmıyor.
- Kullanıcı sayısı arttığında veya ihtiyaçlar karmaşıklaştığında özel bir
  backend'e (Kotlin/Ktor, Node, Python) göç edilebilir — veri modelini bu
  göçü kolaylaştıracak şekilde tasarlıyoruz (aşağıda).

**Alternatif:** Baştan özel backend kurmak daha fazla kontrol verir ama
kurulum/bakım yükü fazladır. Mühendis arkadaşın bu konuda güçlü bir tercihi
varsa (ör. zaten bir stack'te deneyimliyse), bu karar değişebilir — bu, onun
vereceği bir karar.

## Kritik Teknik Karar: Uyum Yüzdesi Nasıl Hesaplanır?

Bu, projenin en önemli mimari sorunu. Saf yaklaşım — "her profil görüntülemede
AI'ya sor" — **ölçeklenmez**: N kullanıcı için N² çift olur, her biri için LLM
çağrısı hem çok yavaş hem çok pahalı olur.

**Önerilen çözüm: İki katmanlı hesaplama**

1. **Embedding tabanlı temel skor (ucuz, hızlı, ölçeklenir)**

   Önemli bir detay: düz kosinüs benzerliği **simetriktir** — A ile B'nin
   vektörleri arasındaki benzerlik, B ile A için de aynı çıkar. Ama konsept
   asimetrik ("A, B'yi %85 görürken B, A'yı %60 görebilir"). Bunu gerçekten
   elde etmek için her kullanıcı için **tek değil, iki vektör** tutuyoruz:

   - **Kimlik vektörü** ("ben kimim"): ilgi alanları, kişilik, değerler —
     onboarding sohbetinden çıkarılır.
   - **Tercih vektörü** ("neyi önemsiyorum"): onboarding sırasında kullanıcıya
     "senin için bir ilişkide/partnerde en önemli şeyler neler, hangileri
     daha az önemli" gibi sorularla, hangi özelliklere ne kadar ağırlık
     verdiği çıkarılır.

   A'nın B'yi görme yüzdesi = **A'nın tercih vektörü** ile **B'nin kimlik
   vektörü** karşılaştırılarak hesaplanır ("B, A'nın aradığı şeylere ne kadar
   uyuyor"). B'nin A'yı görme yüzdesi ise tam tersi: **B'nin tercih vektörü**
   ile **A'nın kimlik vektörü**. İki kullanıcının öncelikleri farklı
   olduğundan (biri "ortak hobi" ye, diğeri "değerlerin örtüşmesi"ne daha çok
   ağırlık veriyor olabilir), sonuç doğal olarak asimetrik çıkar — yapay bir
   rastgelelik eklemeye gerek kalmaz, matematiksel olarak farklıdır.

   - Bu iki vektör de profil oluşturulduğunda/güncellendiğinde **bir kere**
     hesaplanıp Firestore'da saklanır — görüntüleme başına değil.
   - Karşılaştırma (ağırlıklı benzerlik) matematiksel bir işlemdir, LLM
     çağrısı gerektirmez, binlerce profil arasında anında sıralama yapılabilir.
   - Somut örnek: A, "ortak ilgi alanları"na yüksek önem veriyor ama
     "kariyer hedefleri"ne düşük önem veriyorsa; B'nin kimlik vektöründeki
     ilgi alanları öne çıkan boyutlar, A'nın tercih vektöründeki ağırlıklarla
     çarpılıp toplanır ve normalize edilerek bir yüzdeye çevrilir.

2. **LLM ile zenginleştirme (pahalı ama seyrek kullanılan)**
   - Kullanıcı bir profile tıklayıp detayına baktığında veya eşleştiğinde,
     "neden bu kadar uyumlusunuz" gibi **açıklayıcı bir metin** LLM ile
     üretilebilir (sadece talep üzerine, önceden hesaplanmış temel skora
     dayanarak).
   - Bu, AI'ın "hissedilir" katkısını korurken maliyeti kontrol altında
     tutar.

Bu yaklaşım, konseptteki "AI ile hesaplanan, kişiye özel, asimetrik uyum"
hissini korur ama gerçek dünyada çalışabilir bir mühendislik temeline oturtur.

## Veri Modeli (Firestore, ilk taslak)

```
users/{userId}
  - email, createdAt, ...

profiles/{userId}
  - name, age, bio
  - interests: string[]
  - personalitySummary: string        (AI onboarding çıktısı)
  - identityVector: number[]          (kimlik vektörü: "ben kimim")
  - preferenceVector: number[]        (tercih vektörü: "neyi önemsiyorum")
  - updatedAt

onboardingChats/{userId}/messages/{messageId}
  - text, isFromUser, timestamp
  (gizlilik: bu koleksiyonun saklama süresi/erişimi ayrıca netleştirilecek)

likes/{likeId}
  - fromUserId, toUserId, createdAt

matches/{matchId}
  - userIds: [userId1, userId2]
  - createdAt

matches/{matchId}/messages/{messageId}
  - fromUserId, text, timestamp
```

Bu taslak, mühendis arkadaşının ilk günde değiştireceği bir başlangıç noktası
— kesin şema değil.

## AI Entegrasyonu — Güvenlik Notu

**Claude API anahtarı asla Android uygulamasının içine gömülmemeli.** Tüm AI
çağrıları (onboarding sohbeti, embedding üretimi, açıklama metni) **Cloud
Functions üzerinden** yapılmalı; Android uygulaması sadece kendi backend'imize
istek atar, backend AI servisine bağlanır. Aksi halde API anahtarı APK
içinden çıkarılabilir ve kötüye kullanılabilir.

## Kimlik Doğrulama & Mesajlaşma

- **Auth:** Firebase Authentication (e-posta veya telefon numarası ile).
- **Mesajlaşma:** Firestore'da bir koleksiyon + gerçek zamanlı dinleyiciler
  (listener). Android tarafında ek bir kütüphaneye gerek kalmadan çalışır.

## Maliyet Notu

- Firebase'in ücretsiz katmanı (Spark plan) Authentication, Firestore,
  Storage ve Cloud Messaging için MVP ve küçük kullanıcı grupları'nda
  yeterli.
- **Önemli:** Cloud Functions'ı çalıştırmak ve oradan dış bir servise
  (Claude API gibi) istek atmak, projeyi **Blaze (kullandıkça öde) plana**
  yükseltmeyi gerektiriyor — bu bir kredi kartı eklenmesi demek. Blaze'de
  de cömert bir ücretsiz kullanım kotası var (küçük ölçekte muhtemelen
  ödeme çıkmaz), ama bu geçiş bilinçli yapılmalı; AI'ı gerçekten
  bağlayacağımız aşamada (Faz 2) gündeme gelecek, MVP'nin ilk adımlarında
  (Authentication, Firestore kurulumu) gerekmiyor.
- Claude API maliyeti kullanım bazlıdır (token başına ücret). Onboarding
  sohbeti kullanıcı başına bir kez çalıştığı için maliyeti öngörülebilir;
  embedding üretimi de ucuzdur. En büyük maliyet riski, "her görüntülemede
  LLM'e sor" gibi bir tasarım olurdu — bu yüzden yukarıdaki iki katmanlı
  yaklaşım öneriliyor.

## Büyüdüğünde Ne Değişir? (Göç Yolu)

Firebase ile başlamak, aşağıdaki geçişleri **imkansız kılmaz**, sadece
erteler:
- Firestore → PostgreSQL (+ pgvector, embedding aramaları için) özel bir
  backend'e taşınabilir.
- Cloud Functions → kendi sunucunuzda çalışan bir API'ye (Kotlin/Ktor veya
  Node) taşınabilir.
- Veri modelini şimdiden makul tutmak (yukarıdaki gibi), bu göçü kolaylaştırır.

## Mühendis Arkadaşınla Konuşulması Gereken Açık Sorular

1. Firebase mi, yoksa baştan özel bir backend mi? (Onun deneyimi/tercihi
   önemli bir girdi.)
2. Embedding üretimi için hangi model/servis kullanılacak? (Bu, Claude API
   ile mi yoksa ayrı bir embedding servisiyle mi yapılacak — bu konuda
   güncel seçenekleri birlikte değerlendirebiliriz.) Firebase'in kendi
   **Genkit** aracı (LLM entegrasyonunu kolaylaştıran açık kaynak bir
   çerçeve) da bu noktada bir seçenek — Cloud Functions içinde Claude
   API'ye bağlanmayı basitleştirebilir, mühendis arkadaşla
   değerlendirilmeli.
3. Onboarding sohbetinin gizliliği: ham sohbet metni ne kadar süre
   saklanacak, kullanıcı bunu silebilecek mi?
4. İlk sürümde kaç kullanıcı bekleniyor? (Firebase ücretsiz katman
   sınırlarını aşar mı, baştan bütçelenmesi gerekir mi?)
