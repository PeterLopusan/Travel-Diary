package com.peterlopusan.traveldiary.data.models.country

data class VisitedCountry(
    var countryCode: String = "",
    var lastVisitDate: String = "",
    var visitedPlaces: String = "",
    var countryInfo: Country? = null
)
