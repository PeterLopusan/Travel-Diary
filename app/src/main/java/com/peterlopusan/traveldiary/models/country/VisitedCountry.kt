package com.peterlopusan.traveldiary.models.country

data class VisitedCountry(
    var countryCode: String = "",
    var lastVisitDate: String = "",
    var visitedPlaces: String = "",
    var countryInfo: Country? = null
)
