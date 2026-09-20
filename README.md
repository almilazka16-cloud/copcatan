# Sensale — AI Destekli Dating Uygulaması

Sensale, klasik "sağa/sola kaydır" mantığından farklı olarak **yapay zeka ile
üretilen, kişiye özel uyum yüzdeleri** üzerine kurulu bir tanışma uygulaması.
Her kullanıcı, aynı profili farklı bir uyum yüzdesiyle görür — çünkü uyum,
karşılıklı ilgi alanlarına ve AI ile yapılan sohbetten çıkarılan kişilik
verilerine göre **asimetrik ve dinamik** olarak hesaplanır.

İlk platform: **Android (Kotlin, native)**

> **Not:** Uygulamanın adı **Sensale** olarak kesinleşti. Teknik tarafta
> (paket adı `com.copcatan.app`, GitHub deposu `copcatan`) henüz eski isim
> kullanılıyor — bu, mühendis arkadaşla birlikte yapılacak ayrı bir yeniden
> adlandırma işi. Kullanıcı görecek tüm metinler zaten "Sensale" diyor.

Detaylar için:
- [`docs/CONCEPT.md`](docs/CONCEPT.md) — ürün konsepti, mekanikler, temel kararlar
- [`docs/ROADMAP.md`](docs/ROADMAP.md) — fazlı yol haritası
- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — backend/AI mimari önerisi (mühendis arkadaşla gözden geçirilecek)

## Durum

Ürün konsepti netleşti, **Faz 1 (MVP çekirdek)** için Android proje iskeleti
kuruldu. Şu an sahte (mock) verilerle çalışan bir uçtan uca akış var:

- AI ile sohbet ederek profil oluşturma (onboarding) — şu an sabit sorularla,
  gerçek AI entegrasyonu sonraki adım
- Keşif ekranı — uyum yüzdesiyle profil kartları
- Eşleşmeler ekranı
- Basit mesajlaşma ekranı

Henüz **backend, gerçek AI entegrasyonu ve kimlik doğrulama yok**. Bunlar
yol haritasındaki sonraki adımlar (bkz. `docs/ROADMAP.md`).

## Projeyi Açmak (mühendis arkadaş için)

1. [Android Studio](https://developer.android.com/studio) kur.
2. Bu repoyu Android Studio ile aç (kök dizini seç).
3. Gradle senkronizasyonunun bitmesini bekle (ilk açılışta Android SDK
   bileşenlerini otomatik indirir).
4. Bir emülatör veya fiziksel cihazda çalıştır (minSdk 26 / Android 8.0+).

Not: Bu geliştirme ortamında (sandbox) Android SDK ve Google'ın Maven
deposuna erişim olmadığı için proje burada derlenip test edilemedi; kod
elle gözden geçirildi ama ilk gerçek derleme Android Studio'da yapılmalı.

## Ekip

- Ürün sahibi / karar verici: proje kurucusu
- Teknik destek: mühendis arkadaş (devreye girecek)
- Geliştirme: Claude Code ile birlikte
