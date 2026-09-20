# Yol Haritası

Vizyon geniş ama ilerleyiş **yavaş ve fazlı**. Her faz, bir öncekinin üzerine
sağlam şekilde oturmadan bir sonrakine geçilmeyecek.

## Faz 0 — Konsept & Planlama (şu an buradayız)
- [x] Temel ürün konsepti netleştirildi (bkz. `CONCEPT.md`)
- [ ] Teknik mimari kararı (backend yaklaşımı) — mühendis arkadaşla birlikte
- [x] Android proje iskeletinin oluşturulması
- [x] Tasarım/UX yönü: renk kimliği (bordo + krem) ve isim (**Sensale**)
  netleşti — teknik yeniden adlandırma (paket adı, repo adı) hâlâ bekliyor

## Faz 1 — MVP Çekirdek
Amaç: uygulamanın temel döngüsünü çalışır halde görmek.

- Kullanıcı kaydı (e-posta/telefon ile basit auth) — **arayüz mock ile hazır,
  gerçek Firebase Authentication bağlantısı bekliyor**
- Kimlik sohbeti — AI ile "sen kimsin" (onboarding, kimlik vektörünü besler)
- Tercih sohbeti — AI ile "kimi arıyorsun" (tercih vektörünü besler); ilk
  versiyonda basit sorularla, hikayeleştirilmiş senaryolar sonraki bir
  iyileştirme olabilir
- Sert filtreler (dealbreaker'lar, ilişki niyeti uyuşmazlığı) — havuzdan
  tamamen eleme mantığı, MVP'nin karakterinin bir parçası
- Asimetrik uyum yüzdesi hesaplama (ilk versiyon, bkz. `ARCHITECTURE.md`) —
  **kullanıcı kendi yüzdesini görmez**, sadece karşı tarafınkini görür
- Basit keşif ekranı (profilleri uyum yüzdesiyle listeleme)
- Karşılıklı beğenme → eşleşme
- Eşleşen kullanıcılar arası temel mesajlaşma
- **Kapsam dışı (bilinçli olarak sonraya bırakılıyor):** video/kimlik
  doğrulama, buluşma modu, güvenlik bildirimleri, His Odaları, bildirimler,
  premium özellikler

## Faz 2 — Güven & Güvenlik
- Fotoğraf doğrulama: **video ile canlılık kontrolü**, sonraki fotoğrafların
  doğrulanan yüzle eşleşme kontrolü — doğrulanmamış profil kullanılamaz
- Buluşma moduna geçiş (sohbetten ayrı bir arayüze geçen buluşma planlama
  akışı)
- Güvendiğin kişi bildirimi: buluşma detaylarının önceden atanmış güvenilir
  kişiye otomatik iletilmesi (açık kullanıcı onayı ile)
- Kötüye kullanım yaptırımı: ihlallerde 3 aylık engelleme (cihaz bazlı —
  teknik detay mühendis arkadaşla netleştirilecek)
- Kullanıcı raporlama / engelleme
- İçerik moderasyonu (profil ve mesajlarda uygunsuz içerik tespiti)
- Temel gizlilik kontrolleri (AI sohbet verisinin saklanması/silinmesi)

## Faz 3 — Etkileşim & Büyüme
- His Odaları: buluşma sonrası her iki tarafla ayrı ayrı AI sohbeti,
  çelişkili/yeni bilgiyi karşı tarafın kimlik vektörüne geri besleyen
  geri bildirim döngüsü (kötüye kullanıma karşı koruma tasarımı dahil)
- Buluşma öncesi küçük tavsiyeler ("son dakika kurtarıcıları")
- Push bildirimleri (yeni eşleşme, mesaj, buluşma hatırlatmaları)
- AI destekli sohbet başlatma önerileri (ilk mesaj önerisi)
- Tercih sohbetinin hikayeleştirilmiş senaryolarla zenginleştirilmesi

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
