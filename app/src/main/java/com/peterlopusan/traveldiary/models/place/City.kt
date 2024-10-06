package com.peterlopusan.traveldiary.models.place

import com.squareup.moshi.Json

data class City(
    var name: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var country: String? = null,
    var population: String? = null,
    @Json(name = "is_capital") val isCapital: Boolean? = null,
)
