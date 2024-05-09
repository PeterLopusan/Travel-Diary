package com.peterlopusan.traveldiary.data.models.flight

import com.squareup.moshi.Json

data class Airport(
    val icao: String? = null,
    val iata: String? = null,
    val name: String? = null,
    val city: String? = null,
    val region: String? = null,
    val country: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    @Json(name = "elevation_ft") val elevation: Double? = null,
)
