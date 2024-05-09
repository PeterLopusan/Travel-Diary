package com.peterlopusan.traveldiary.api

import com.peterlopusan.traveldiary.data.models.country.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountryApi {

    @GET(GET_COUNTRIES_LIST_URL)
    suspend fun getCountriesList(): Response<MutableList<Country>>

}