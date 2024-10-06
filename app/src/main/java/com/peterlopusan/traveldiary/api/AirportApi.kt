package com.peterlopusan.traveldiary.api

import com.peterlopusan.traveldiary.models.flight.Airport
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface AirportApi {

    @Headers("$NINJAS_API_CONSTANT: $NINJAS_API_KEY")
    @GET(GET_AIRPORTS_LIST_URL)
    suspend fun getAirportsListByName(@Query(AIRPORT_NAME_QUERY) name: String): Response<MutableList<Airport>>?

    @Headers("$NINJAS_API_CONSTANT: $NINJAS_API_KEY")
    @GET(GET_AIRPORTS_LIST_URL)
    suspend fun getAirportsListByCity(@Query(AIRPORT_CITY_QUERY) city: String): Response<MutableList<Airport>>?

    @Headers("$NINJAS_API_CONSTANT: $NINJAS_API_KEY")
    @GET(GET_AIRPORTS_LIST_URL)
    suspend fun getAirportsListByIata(@Query(AIRPORT_IATA_QUERY) iata: String): Response<MutableList<Airport>>?
}