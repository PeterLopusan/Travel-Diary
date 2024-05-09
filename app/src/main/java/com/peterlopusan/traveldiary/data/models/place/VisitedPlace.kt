package com.peterlopusan.traveldiary.data.models.place

data class VisitedPlace(
    var id: String? = null,
    var lastVisitDate: String? = null,
    var note: String? = null,
    var imageUrl: String? = null,
    var visitedCity: City? = null,
    var visitedPlace: Place? = null
)


data class Place(
    var name: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var country: String? = null,
    var region: String? = null
)

