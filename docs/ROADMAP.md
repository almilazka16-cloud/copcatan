# Yol Haritası

Vizyon geniş ama ilerleyiş **yavaş ve fazlı**. Her faz, bir öncekinin üzerine
sağlam şekilde oturmadan bir sonrakine geçilmeyecek.

## Faz 0 — Konsept & Planlama (şu an buradayız)
- [x] Temel ürün konsepti netleştirildi (bkz. `CONCEPT.md`)
- [ ] Teknik mimari kararı (backend yaklaşımı) — mühendis arkadaşla birlikte
- [ ] Android proje iskeletinin oluşturulması
- [ ] Tasarım/UX yönü (renk, isim, marka kimliği) — opsiyonel, istenirse

## Faz 1 — MVP Çekirdek
Amaç: uygulamanın temel döngüsünü çalışır halde görmek.

- Kullanıcı kaydı (e-posta/telefon ile basit auth) — **arayüz mock ile hazır,
  gerçek Firebase Authentication bağlantısı bekliyor**
- AI ile sohbet ederek profil oluşturma (onboarding)
- Asimetrik uyum yüzdesi hesaplama (ilk versiyon: ilgi alanı bazlı, basit)
- Basit keşif ekranı (profilleri uyum yüzdesiyle listeleme)
- Karşılıklı beğenme → eşleşme
- Eşleşen kullanıcılar arası temel mesajlaşma
- **Kapsam dışı (bilinçli olarak sonraya bırakılıyor):** fotoğraf/kimlik
  doğrulama, bildirimler, video, premium özellikler

## Faz 2 — Güven & Güvenlik
- Fotoğraf doğrulama (gerçek kişi kontrolü)
- Kullanıcı raporlama / engelleme
- İçerik moderasyonu (profil ve mesajlarda uygunsuz içerik tespiti)
- Temel gizlilik kontrolleri (AI sohbet verisinin saklanması/silinmesi)

## Faz 3 — Etkileşim & Büyüme
- Push bildirimleri (yeni eşleşme, mesaj, uyum güncellemesi)
- Profilin zamanla AI ile gelişmesi (kullanım verisiyle uyum skorunun
  güncellenmesi)
- AI destekli sohbet başlatma önerileri (ilk mesaj önerisi)
- Uygulama içi geri bildirim döngüsü (kullanıcı "bu uyum neden böyle"
  sorusuna dair içgörü alabilsin)

## Faz 4 — Monetizasyon & Genişleme
- Premium üyelik (ör: günlük beğeni limiti, kimin beğendiğini görme)
- Video profil desteği
- Gelişmiş AI içgörüleri (kişilik analizi raporu, ilişki tavsiyeleri)
- (İleride, talep olursa) iOS versiyonu

## Teknik Yaklaşım Notları

- **Platform:** Native Android (Kotlin) — karar verildi.
- **Backend/AI altyapısı:** Henüz kesinleşmedi, mühendis arkadaşla
  konuşulacak. MVP için hız kazandıracak seçenekler:
  - Firebase (Auth + Firestore + Cloud Functions) + bir LLM API'si (ör.
    Claude API) ile hızlı prototipleme
  - Veya özel bir backend (Kotlin/Node/Python) — daha fazla kontrol ama
    daha yavaş kurulum
  - Bu karar Faz 0'ın çıktılarından biri olacak.
- **AI sohbet & uyum hesaplama:** Bir LLM API'si (ör. Claude API) ile
  onboarding sohbeti ve uyum skorlama mantığı kurulacak. Detaylar Faz 1'de
  netleşecek.

## Nasıl İlerleyeceğiz

1. Her faz kendi içinde küçük, test edilebilir adımlara bölünecek.
2. Kod tarafında ben (Claude Code) yazacağım; kararlar ve yön sizin
   (proje sahibi + mühendis arkadaş).
3. Mühendis arkadaş devreye girdiğinde, teknik mimari kararlarını (backend,
   API tasarımı, veri modeli) birlikte gözden geçireceğiz.
4. İlerleme bu dosyada işaretlenerek takip edilecek.
