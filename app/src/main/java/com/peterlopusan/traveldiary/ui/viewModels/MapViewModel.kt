package com.peterlopusan.traveldiary.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.data.models.country.VisitedCountry
import com.peterlopusan.traveldiary.data.models.map.CountryBorder
import com.peterlopusan.traveldiary.utils.showLogs

class MapViewModel : ViewModel() {

    fun getMyHomelandCountryBorder(countryCode: String): MutableList<MutableList<LatLng>> {
        val coordinates = mutableListOf<MutableList<LatLng>>()

        val countryBorder = getJsonFile(countryCode)?.let { readJsonFile(it) }

        countryBorder?.let {
            countryBorder.coordinates.forEach { first ->
                first.forEach { second ->
                    val list = mutableListOf<LatLng>()
                    second.forEach { third ->
                        list.add(LatLng(third[1], third[0]))
                    }
                    coordinates.add(list)
                }
            }
        }

        return coordinates
    }

    fun getCountriesBorder(countryList: MutableList<VisitedCountry>): MutableList<MutableList<LatLng>> {
        val coordinates = mutableListOf<MutableList<LatLng>>()

        countryList.forEach { visitedCountry ->
            val countryBorder = getJsonFile(visitedCountry.countryCode)?.let { readJsonFile(it) }

            countryBorder?.let {
                countryBorder.coordinates.forEach { first ->
                    first.forEach { second ->
                        val list = mutableListOf<LatLng>()
                        second.forEach { third ->
                            list.add(LatLng(third[1], third[0]))
                        }
                        coordinates.add(list)
                    }
                }
            }
        }

        return coordinates
    }


    private fun readJsonFile(resourceId: Int): CountryBorder {
        return try {
            val inputStream = MainActivity.instance.resources.openRawResource(resourceId)
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()
            return gson.fromJson(jsonText, CountryBorder::class.java)
        } catch (e: Exception) {
            showLogs(e.message)
            CountryBorder(coordinates = listOf())
        }
    }


    private fun getJsonFile(countryShortname: String): Int? {
        return when (countryShortname) {
            "AE" -> R.raw.arab_emirates
            "AT" -> R.raw.austria
            "AL" -> R.raw.albania
            "BG" -> R.raw.bulgaria
            "BR" -> R.raw.brazil
            "CZ" -> R.raw.czechia
            "CV" -> R.raw.cape_verde
            "DE" -> R.raw.germany
            "DM" -> R.raw.dominica
            "ES" -> R.raw.spain
            "EG" -> R.raw.egypt
            "FR" -> R.raw.france
            "HU" -> R.raw.hungary
            "HR" -> R.raw.croatia
            "GB" -> R.raw.united_kingdom
            "GQ" -> R.raw.equatorial_guinea
            "GR" -> R.raw.greece
            "GM" -> R.raw.gambia
            "IT" -> R.raw.italy
            "LV" -> R.raw.latvia
            "MD" -> R.raw.moldova
            "ME" -> R.raw.montenegro
            "MK" ->  R.raw.north_macedonia
            "MY" ->  R.raw.malaysia
            "MZ" ->  R.raw.mozambique
            "NR" -> R.raw.nauru
            "NG" -> R.raw.nigeria
            "YT" -> R.raw.mayotte
            "PL" -> R.raw.poland
            "PW" -> R.raw.palau
            "RO" -> R.raw.romania
            "RS" -> R.raw.serbia
            "SK" -> R.raw.slovakia
            "SO" -> R.raw.somalia
            "TR" -> R.raw.turkey
            "UA" -> R.raw.ukraine
            "VA" -> R.raw.vatican
            "US" -> R.raw.united_states
            "YE" -> R.raw.yemen


            else -> null
        }
    }
}