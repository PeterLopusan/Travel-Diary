package com.peterlopusan.traveldiary.api

import com.peterlopusan.traveldiary.models.place.City
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CityApi {

    @Headers("$NINJAS_API_CONSTANT: $NINJAS_API_KEY")
    @GET(GET_CITIES_LIST_URL)
    suspend fun getCitiesListByName(@Query(CITIES_NAME_QUERY) name: String, @Query(CITIES_LIMIT_QUERY) limit: Int = 30): Response<MutableList<City>>?


    @Headers("$NINJAS_API_CONSTANT: $NINJAS_API_KEY")
    @GET(GET_CITIES_LIST_URL)
    suspend fun getCitiesListByNameLatLon(
        @Query(CITIES_NAME_QUERY) name: String,
        @Query(CITIES_MIN_LAT_QUERY) latitudeMin: String,
        @Query(CITIES_MIN_LON_QUERY) longitudeMin: String,
        @Query(CITIES_MAX_LAT_QUERY) latitudeMax: String,
        @Query(CITIES_MAX_LON_QUERY) longitudeMax: String,
        @Query(CITIES_LIMIT_QUERY) limit: Int = 30
    ): Response<MutableList<City>>?
}