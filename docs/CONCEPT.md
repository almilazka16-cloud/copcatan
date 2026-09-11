# Ürün Konsepti

## Temel Fikir

Piyasadaki dating uygulamalarının çoğu (Tinder, Bumble, Hinge...) statik bir
"beğen/beğenme" akışına dayanıyor. CopCatan'ın farkı:

> Aynı profil, farklı kullanıcıların ekranında **farklı bir uyum yüzdesiyle**
> görünür. Bu yüzde, kullanıcıların ilgi alanları ve AI ile yapılan sohbetten
> çıkarılan kişilik/tercih verilerine göre hesaplanır ve zamanla, kullanıcı
> uygulamayı kullandıkça gelişir/güncellenir.

Bu, statik bir "eşleşme skoru" değil; **her kullanıcı çifti için tek yönlü ve
kişiselleştirilmiş bir uyum tahmini**dir.

## Temel Mekanikler

### 1. AI ile Profil Oluşturma
- Kullanıcı kayıt olurken bir AI ile **sohbet ederek** profilini oluşturur
  (form doldurmak yerine, mülakat/sohbet tarzında bir onboarding).
- AI, bu sohbetten ilgi alanlarını, kişilik özelliklerini, değerlerini ve
  tercihlerini çıkarır ve yapılandırılmış bir "profil verisi" haline getirir.
- Bu profil verisi statik değildir: kullanıcı uygulamayı kullandıkça
  (yeni sohbetler, etkileşimler, geri bildirimler) profil zenginleşir ve
  uyum hesaplamaları güncellenir.

### 2. Asimetrik Uyum Yüzdesi
- Kullanıcı A, kullanıcı B'nin profilini **%85 uyumlu** olarak görebilirken,
  B aynı anda A'yı **%60 uyumlu** olarak görebilir. Yüzdeler simetrik değildir.
- Uyum hesaplaması, iki kullanıcının profil verilerinin (ilgi alanı, değerler,
  kişilik) AI tarafından karşılaştırılmasıyla üretilir.
- Kullanıcılar bu farkı görür ve zamanla "neden bu kadar uyumlu/uyumsuz
  görünüyorum" sorusuna karşı meraklı kalır — bu da etkileşimi artıran bir
  tasarım unsuru.

### 3. Eşleşme ve Mesajlaşma
- Keşif ekranında kullanıcılar, kendilerine göre hesaplanmış uyum yüzdesine
  göre profilleri görür (yüksekten düşüğe sıralanabilir veya karışık
  gösterilebilir — bu ayrı bir tasarım kararı).
- **Karşılıklı beğenme (like) şartı korunuyor**: iki taraf da birbirini
  beğenirse eşleşme oluşur ve mesajlaşma açılır. Yani uyum yüzdesi bir
  "öneri/sıralama" sinyali, ama sohbet başlatmak için hâlâ karşılıklı onay
  gerekiyor (Tinder'a yakın ama uyum hesaplaması tamamen farklı).

## Kapsam Yaklaşımı

Uzun vadeli vizyon geniş (video profiller, premium özellikler, gelişmiş AI
içgörüleri vb.) ama geliştirme **yavaş ve sistemli** ilerleyecek: önce sağlam
bir çekirdek, sonra üstüne katman katman ekleme. Detaylı fazlar için
[`ROADMAP.md`](ROADMAP.md) dosyasına bakın.

## Açık Sorular (Zamanla Netleşecek)

Bunlar şu an karar verilmemiş, geliştirme ilerledikçe netleştireceğimiz
noktalar:

- ~~Uyum yüzdesi hesaplaması tam olarak hangi verilerden besleniyor?~~
  **Netleşti** (bkz. `ARCHITECTURE.md`): her kullanıcı için iki ayrı vektör
  tutulacak — "ben kimim" (kimlik vektörü) ve "neyi önemsiyorum" (tercih
  vektörü). A'nın B'yi görme yüzdesi, A'nın tercihleriyle B'nin kimliği
  karşılaştırılarak hesaplanır; bu asimetrikliği matematiksel olarak
  garanti eder. Açık kalan nokta: davranışsal veriler (mesajlaşma tarzı,
  kullanım alışkanlıkları) ileride bu vektörlere dahil edilecek mi —
  bu, Faz 3'te ele alınacak.
- Keşif ekranında sıralama mantığı: en yüksek uyumdan mı başlasın, yoksa
  çeşitlilik için karışık mı sunulsun?
- AI sohbet onboarding'i ne kadar sürsün / kaç soru içersin?
- Profil verisi ne sıklıkla yeniden hesaplanır (her mesajdan sonra mı,
  periyodik mi)?
- Gizlilik: AI ile yapılan sohbetin içeriği ne kadarı görünür/saklanır?

Bu sorular ROADMAP'teki ilgili fazlarda ele alınacak.
