package com.peterlopusan.traveldiary.models.flight

data class CompletedFlight (
    var id: String? = null,
    var departureAirport: Airport? = null,
    var arrivalAirport: Airport? = null,
    var flightDate: String = "",
    var durationHours: String = "",
    var durationMinutes: String = ""
)