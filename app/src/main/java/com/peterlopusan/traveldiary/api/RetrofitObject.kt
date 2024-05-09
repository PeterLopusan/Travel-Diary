package com.peterlopusan.traveldiary.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient: OkHttpClient = OkHttpClient()
    .newBuilder()
    .connectTimeout(120, TimeUnit.SECONDS)
    .readTimeout(120, TimeUnit.SECONDS)
    .build()


private val retrofitCountry = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_API_URL_COUNTRIES)
    .client(okHttpClient)
    .build()

private val retrofitAirport = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_API_URL_NINJAS)
    .client(okHttpClient)
    .build()

object RetrofitObject {
    val countryApi: CountryApi by lazy {
        retrofitCountry.create(CountryApi::class.java)
    }

    val airportApi: AirportApi by lazy {
        retrofitAirport.create(AirportApi::class.java)
    }

    val cityApi: CityApi by lazy {
        retrofitAirport.create(CityApi::class.java)
    }
}