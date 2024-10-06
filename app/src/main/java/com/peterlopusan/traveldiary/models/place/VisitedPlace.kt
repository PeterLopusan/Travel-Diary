package com.peterlopusan.traveldiary.models.place

data class VisitedPlace(
    var id: String? = null,
    var lastVisitDate: String? = null,
    var note: String? = null,
    var imageUrl: String? = null,
    var visitedCity: City? = null,
    var visitedPlace: Place? = null
)




