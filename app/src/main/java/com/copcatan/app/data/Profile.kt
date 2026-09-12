package com.copcatan.app.data

data class Profile(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val interests: List<String>,
    /**
     * Bu uygulamanın kalbi: bu yüzde, görüntüleyen kullanıcıya özeldir.
     * Aynı profil başka bir kullanıcının ekranında farklı bir yüzdeyle görünür.
     * Şu an sahte (mock) veri; gerçek sürümde backend'deki AI tarafından,
     * iki kullanıcının profil verisi karşılaştırılarak hesaplanacak.
     */
    val compatibilityPercent: Int
)
