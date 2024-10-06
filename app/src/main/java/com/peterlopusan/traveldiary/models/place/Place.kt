package com.peterlopusan.traveldiary.models.place

data class Place(
    var name: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var country: String? = null,
    var region: String? = null
)
