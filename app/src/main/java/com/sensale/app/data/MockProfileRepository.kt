package com.sensale.app.data

/**
 * Faz 1'de gerçek bir backend/AI servisiyle değiştirilecek geçici veri kaynağı.
 * Amaç: ekranların uçtan uca akışını (keşif -> eşleşme -> sohbet) sahte veriyle
 * görünür kılmak.
 */
object MockProfileRepository {

    fun getDiscoveryProfiles(): List<Profile> = listOf(
        Profile(
            id = "1",
            name = "Ada",
            age = 27,
            bio = "Kitap kurdu, kahve bağımlısı, dağcılık meraklısı.",
            interests = listOf("Kitap", "Kahve", "Doğa Yürüyüşü"),
            compatibilityPercent = 92
        ),
        Profile(
            id = "2",
            name = "Deniz",
            age = 24,
            bio = "Müzik prodüktörü, gece kuşuyum, köpeğim var.",
            interests = listOf("Müzik", "Köpekler", "Konserler"),
            compatibilityPercent = 78
        ),
        Profile(
            id = "3",
            name = "Ela",
            age = 29,
            bio = "Yoga eğitmeni, seyahat etmeyi seviyorum.",
            interests = listOf("Yoga", "Seyahat", "Fotoğrafçılık"),
            compatibilityPercent = 65
        )
    )

    fun getMatches(): List<Profile> = listOf(getDiscoveryProfiles().first())
}
