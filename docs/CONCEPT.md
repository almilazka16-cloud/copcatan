# Ürün Konsepti

## Temel Fikir

Piyasadaki dating uygulamalarının çoğu (Tinder, Bumble, Hinge...) statik bir
"beğen/beğenme" akışına dayanıyor. CopCatan'ın farkı:

> Aynı profil, farklı kullanıcıların ekranında **farklı bir uyum yüzdesiyle**
> görünür. Bu yüzde, kullanıcının kim olduğu ve karşı taraftan ne beklediği
> AI ile yapılan sohbetlerden çıkarılarak hesaplanır ve kullanıcı uygulamayı
> kullandıkça (özellikle buluşma sonrası geri bildirimlerle) güncellenir.

Bu, statik bir "eşleşme skoru" değil; **her kullanıcı çifti için tek yönlü ve
kişiselleştirilmiş bir uyum tahmini**dir. Uygulama arkadaşlık ve romantik
ilişki arayan kullanıcılara hitap eder; form doldurtmaz, seçenek listesi
sunmaz — her şey sohbet üzerinden ilerler.

## Kullanıcı Yolculuğu

Aşağıdaki sıra, bir kullanıcının uygulamayla ilk temasından bir buluşma
sonrasına kadar geçireceği akışı özetliyor.

### 1. Kayıt ve Doğrulama
- Giriş e-posta veya telefon numarasıyla, doğrulama kodu ile yapılır (bkz.
  Android iskeletindeki mock `AuthScreen`).
- **18 yaş altı kullanamaz** — yaş/kimlik doğrulaması zorunlu.
- Fotoğraf doğrulaması **video ile canlılık kontrolü** üzerinden yapılır.
  Sonradan eklenen bir fotoğraf, doğrulanan yüzle eşleşmiyorsa kabul
  edilmez. **Doğrulanmamış bir profil uygulamada hiçbir şekilde
  kullanılamaz** — bu opsiyonel bir adım değil, zorunlu bir kapı.
- Profilde kullanıcının değiştirebileceği tek alanlar: **isim, yaş,
  fotoğraflar**. Geri kalan her şey (kişilik özeti, ilgi alanları, tercih
  verisi) AI sohbetlerinden türetilir, kullanıcı tarafından elle
  düzenlenemez.

### 2. Kimlik Sohbeti ("Sen kimsin?")
- AI ile serbest bir sohbet: kullanıcı kendini anlatır, AI soruları sohbetin
  akışına göre yönlendirir (sabit bir form/anket değil).
- AI bu sohbetten kullanıcının ilgi alanlarını, değerlerini ve kişilik
  özelliklerini (ör. ne kadar uyumlu, cömert, kibirli göründüğü gibi
  boyutları) çıkarır ve bunları **kimlik vektörüne** dönüştürür.
- **Kullanıcı bu iç değerlendirmeyi hiçbir zaman görmez.** Amaç kullanıcıyı
  etiketlemek değil, AI'ın onu daha iyi eşleştirebilmesi.

### 3. Tercih Sohbeti ("Kimi arıyorsun?")
- Ayrı bir sohbet: AI, kullanıcının karşı tarafta ne aradığını sorar.
  Bazı sorular doğrudan ("İlişkide senin için en önemli şey ne?"), bazıları
  **hikayeleştirilmiş senaryolar** üzerinden sorulur (ör. "Şöyle bir çift
  şöyle bir durum yaşamış, sence bu ne kadar sürdürülebilir?" gibi bir
  anlatı üzerinden mesafe toleransı, yaşam tarzı uyumu gibi konular ölçülür).
- Bu sohbetten çıkan veri, **tercih vektörünü** oluşturur — "neyi
  önemsiyorum" bilgisi.
- Bu aşamada iki farklı sinyal toplanır:
  - **Yumuşak tercihler** → uyum yüzdesini etkiler (ağırlıklandırma).
  - **Sert filtreler / dealbreaker'lar** → tamamen eleyicidir. Örnek:
    kullanıcı "sigara içen biriyle kesinlikle olmam" derse, sigara içtiğini
    belirtmiş biri o kullanıcının havuzuna **hiç girmez** — düşük yüzdeyle
    bile görünmez.
  - **İlişki niyeti uyuşmazlığı** da sert bir filtredir: "sadece anlık/an
    ilişkisi" isteyen biriyle "ciddi ilişki/evlilik" isteyen biri birbirinin
    havuzuna girmez.

### 4. Asimetrik Uyum Yüzdesi
- A'nın B'yi görme yüzdesi = A'nın tercih vektörü × B'nin kimlik vektörü
  (bkz. `ARCHITECTURE.md`). B'nin A'yı görme yüzdesi bunun tam tersi ve
  genelde **farklı bir sayı** çıkar — çünkü iki kişinin öncelikleri
  birbirinden bağımsızdır.
  - Örnek: Ali'nin havuzunda Ayşe %60, Fatma %10 görünebilir (Ali'nin
    tercihlerine göre). Aynı anda Ayşe'nin havuzunda Ali %40 görünebilir
    (Ayşe'nin kendi, farklı, tercihlerine göre).
- **Kullanıcı kendi yüzdesini asla göremez** — yani Ali, başkalarının onu
  kaçta gördüğünü bilemez. Bu bilinçli bir tasarım kararı: amaç kimsede
  "ben yetersizim" hissi yaratmamak. Yüzde sadece "karşındakine bakarken"
  bir sinyal olarak gösterilir, kendine bakarken değil.
- Sert filtreler (madde 3) ile elenen kişiler havuzda hiç görünmez; yüzde
  sadece filtreyi geçen kişiler arasında anlamlıdır.

### 5. Keşif ve Eşleşme
- Keşif havuzu; uyum yüzdesi, yaş/konum uygunluğu, iş hayatı/yaşam tarzı
  denkliği ve sert filtrelerin kesişimiyle oluşur.
- Karşılıklı beğenme (like) ile eşleşme gerçekleşir ve mesajlaşma açılır.

### 6. Buluşma Moduna Geçiş
- Eşleşen kullanıcılar sohbet ederken, belirli bir noktadan sonra (ör. belli
  sayıda mesajdan sonra) uygulama bir **"buluşmaya geçelim mi?"** seçeneği
  sunar.
- Bu seçilirse sohbet, **ayrı bir arayüze** geçer: buluşma yeri/zamanı gibi
  pratik detayların konuşulduğu, normal sohbetten görsel olarak ayrışan bir
  mod.

### 7. Güvenlik — Güvendiğin Kişi Bildirimi
- Kullanıcılar önceden bir güvendikleri kişiyi (acil durum kişisi) atar.
- Buluşma detayları (kiminle, nerede) netleştiğinde, **her iki tarafın da
  güvendiği kişisine otomatik bir bilgilendirme** gider (ör. "Ayşe, Ali ile
  Kızılay'da buluşacak").
- Amaç: taraflardan birine bir şey olursa veya ulaşılamazsa, güvenilen kişi
  bilgi sahibi olsun.
- Bu özellik açık kullanıcı onayı gerektirir (KVKK/gizlilik konusu — bkz.
  ileride ele alınacak "Yasal/Güvenlik" çalışması).

### 8. Buluşma Öncesi Küçük Tavsiyeler
- Buluşma ayarlandıktan sonra, cinsiyete ve konuşulan konulara özel küçük
  öneriler sunulur (ör. "konuştuğunuz kitabı götürmek tatlı bir jest
  olabilir", parfüm/makyaj ipuçları gibi). "Son dakika kurtarıcıları" olarak
  düşünülüyor — zorunlu değil, hafif ve keyifli bir dokunuş.

### 9. Buluşma Sonrası — "His Odaları"
- Buluşmadan sonra (aynı gün veya ertesi gün) her iki taraf **ayrı ayrı**
  AI ile buluşmayı konuşur — samimi, "arkadaşına anlatır gibi" bir sohbet.
- AI sunucu tarafında **her iki tarafı da tanıdığı için**, çelişkileri
  yakalayabilir: biri kendini olduğundan farklı tanıtmışsa, ya da beklenmedik
  olumlu/olumsuz bir şey ortaya çıkmışsa, bu bilgi **karşı tarafın kimlik
  vektörünü günceller**.
- Bu, sistemin öğrenen bir geri bildirim döngüsü olmasını sağlar: profil
  sadece ilk sohbetle sabitlenmiş bir şey değil, gerçek buluşma
  deneyimleriyle zaman içinde güncellenen bir şey.
- Hassas bir özellik olduğu için kötüye kullanıma (asılsız/kötü niyetli
  geri bildirim) karşı nasıl korunacağı ayrıca tasarlanmalı.

### 10. Kötüye Kullanım ve Yaptırımlar
- Küfür, hakaret gibi ihlallerde **3 aylık bir engelleme** uygulanır.
- Engelleme cihaz bazlı düşünülüyor (yeni hesapla kolayca aşılmasın diye) —
  bunun teknik olarak nasıl uygulanacağı (cihaz parmak izi vb.) mühendis
  arkadaşla ayrıca konuşulacak bir konu.
- Engellenen kullanıcı uygulamayı tekrar açtığında kalan süresini gösteren
  bir ekranla karşılaşır.

## Kapsam Yaklaşımı

Vizyon geniş ama geliştirme **yavaş ve sistemli** ilerleyecek: önce sağlam
bir çekirdek (kimlik + tercih sohbeti, asimetrik yüzde, temel eşleşme/sohbet),
sonra güven/güvenlik katmanı, sonra etkileşimi derinleştiren özellikler
(His Odaları, buluşma tavsiyeleri). Hangi mekaniğin hangi fazda olduğu için
[`ROADMAP.md`](ROADMAP.md) dosyasına bakın.

## Açık Sorular (Zamanla Netleşecek)

- ~~Uygulama girişi (ilk açılış deneyimi)~~ **Netleşti:** 3 kaydırmalı,
  atlanabilir bir tanıtım akışı (asimetrik uyum, sohbet tabanlı onboarding,
  buluşma güvenliği) giriş ekranından önce gösteriliyor.
- His Odaları'ndaki geri bildirim, kötü niyetli/asılsız kullanıma karşı nasıl
  korunacak? (Faz 3'te ele alınacak.)
- Cihaz bazlı engelleme teknik olarak nasıl uygulanacak? (Mühendis arkadaşla
  görüşülecek.)
- Keşif ekranında sıralama mantığı: en yüksek uyumdan mı başlasın, yoksa
  çeşitlilik için karışık mı sunulsun?
- Profil verisi (kimlik/tercih vektörleri) ne sıklıkla yeniden hesaplanır —
  her buluşma geri bildiriminden sonra mı, periyodik mi?
- Gizlilik: AI ile yapılan sohbetlerin (kimlik, tercih, His Odaları) içeriği
  ne kadar süre saklanır, kullanıcı silebilir mi?

Bu sorular ROADMAP'teki ilgili fazlarda ele alınacak.
