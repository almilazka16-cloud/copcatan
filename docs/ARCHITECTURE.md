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
   - Her kullanıcının profil verisi (AI onboarding sohbetinden çıkarılan
     ilgi alanları/kişilik özeti) bir **embedding vektörüne** dönüştürülür
     ve Firestore'da saklanır. Bu, profil oluşturulduğunda/güncellendiğinde
     bir kere yapılır — görüntüleme başına değil.
   - İki kullanıcı arasındaki temel uyum, bu vektörlerin **kosinüs
     benzerliği** ile anında hesaplanır (matematiksel işlem, LLM çağrısı
     gerektirmez). Bu, asimetrikliği doğal olarak destekler: A'nın
     vektörüyle B'nin vektörü arasındaki benzerlik, farklı ağırlıklandırma
     / normalize etme mantığıyla her kullanıcı için farklı bir yüzdeye
     dönüştürülebilir.
   - Bu sayede keşif ekranı, binlerce profil arasında anında sıralama
     yapabilir.

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
  - embeddingVector: number[]         (uyum hesaplaması için)
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

- Firebase'in ücretsiz katmanı MVP ve küçük kullanıcı grupları için yeterli.
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
   güncel seçenekleri birlikte değerlendirebiliriz.)
3. Onboarding sohbetinin gizliliği: ham sohbet metni ne kadar süre
   saklanacak, kullanıcı bunu silebilecek mi?
4. İlk sürümde kaç kullanıcı bekleniyor? (Firebase ücretsiz katman
   sınırlarını aşar mı, baştan bütçelenmesi gerekir mi?)
