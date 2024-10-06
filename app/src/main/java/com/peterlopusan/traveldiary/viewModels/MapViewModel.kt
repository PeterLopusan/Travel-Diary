package com.peterlopusan.traveldiary.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.models.country.VisitedCountry
import com.peterlopusan.traveldiary.models.map.CountryBorder
import com.peterlopusan.traveldiary.models.place.VisitedPlace
import com.peterlopusan.traveldiary.utils.showLogs

class MapViewModel : ViewModel() {

    /********************* Places *********************/
    fun getPlacesForMarkers(visitedPlace: MutableList<VisitedPlace>): MutableList<Pair<String, LatLng>> {
        val places = mutableListOf<Pair<String, LatLng>>()

        visitedPlace.forEach { place ->
            if (place.visitedCity != null) {
                places.add(Pair(place.visitedCity?.name ?: "", LatLng(place.visitedCity?.latitude ?: 0.0, place.visitedCity?.longitude ?: 0.0)))
            } else if (place.visitedPlace != null) {
                places.add(Pair(place.visitedPlace?.name ?: "", LatLng(place.visitedPlace?.latitude ?: 0.0, place.visitedPlace?.longitude ?: 0.0)))
            }
        }
        return places.distinct().toMutableList()
    }


    /********************* Countries *********************/
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
            "AM" -> R.raw.armenia
            "AU" -> R.raw.australia
            "AF" -> R.raw.afghanistan
            "AO" -> R.raw.angola
            "AE" -> R.raw.arab_emirates
            "AR" -> R.raw.argentina
            "AT" -> R.raw.austria
            "AL" -> R.raw.albania
            "AG" -> R.raw.antigua
            "AD" -> R.raw.andorra
            "AZ" -> R.raw.azerbaijan
            "BG" -> R.raw.bulgaria
            "BA" -> R.raw.bosna
            "BD" -> R.raw.bangladesh
            "BN" -> R.raw.brunei
            "BW" -> R.raw.bostwana
            "BT" -> R.raw.bhutan
            "BI" -> R.raw.burundi
            "BH" -> R.raw.bahrain
            "BE" -> R.raw.belgium
            "BZ" -> R.raw.belize
            "BR" -> R.raw.brazil
            "BO" -> R.raw.bolivia
            "BF" -> R.raw.burkina_faso
            "BY" -> R.raw.belarus
            "BS" -> R.raw.bahamas
            "BB" -> R.raw.barbados
            "CZ" -> R.raw.czechia
            "CA" -> R.raw.canada
            "CN" -> R.raw.china
            "CL" -> R.raw.chile
            "CG" -> R.raw.congo
            "CH" -> R.raw.switzerland
            "CO" -> R.raw.colombia
            "CR" -> R.raw.costa_rica
            "CM" -> R.raw.cameroon
            "CY" -> R.raw.cyprus
            "CF" -> R.raw.central_african_republic
            "CV" -> R.raw.cape_verde
            "CI" -> R.raw.ivory_coast
            "CU" -> R.raw.cuba
            "DE" -> R.raw.germany
            "DN" -> R.raw.denmark
            "DZ" -> R.raw.algeria
            "DJ" -> R.raw.djibouti
            "DM" -> R.raw.dominica
            "ES" -> R.raw.spain
            "ER" -> R.raw.eritrea
            "EG" -> R.raw.egypt
            "ET" -> R.raw.ethiopia
            "FR" -> R.raw.france
            "FJ" -> R.raw.fiji
            "FM" -> R.raw.micronesia
            "FI" -> R.raw.finland
            "HU" -> R.raw.hungary
            "HN" -> R.raw.honduras
            "HR" -> R.raw.croatia
            "HT" -> R.raw.haiti
            "GB" -> R.raw.united_kingdom
            "GT" -> R.raw.guatemala
            "GY" -> R.raw.guyana
            "GQ" -> R.raw.equatorial_guinea
            "GD" -> R.raw.grenada
            "GH" -> R.raw.ghana
            "GW" -> R.raw.guinea
            "GR" -> R.raw.greece
            "GM" -> R.raw.gambia
            "IT" -> R.raw.italy
            "IL" -> R.raw.israel
            "IR" -> R.raw.iran
            "IE" -> R.raw.ireland
            "IN" -> R.raw.india
            "JO" -> R.raw.jordan
            "JM" -> R.raw.jamaica
            "KW" -> R.raw.kuwait
            "KI" -> R.raw.kiribati
            "KN" -> R.raw.kitts
            "KH" -> R.raw.cambodia
            "KE" -> R.raw.kenya
            "KG" -> R.raw.kyrgyzstan
            "KZ" -> R.raw.kazakhstan
            "KP" -> R.raw.north_korea
            "KR" -> R.raw.south_korea
            "KM" -> R.raw.comoros
            "LV" -> R.raw.latvia
            "LB" -> R.raw.lebanon
            "LK" -> R.raw.sri_lanka
            "LS" -> R.raw.lesotho
            "LY" -> R.raw.libya
            "LT" -> R.raw.lithuania
            "LC" -> R.raw.saint_lucia
            "LA" -> R.raw.laos
            "LI" -> R.raw.liechtenstein
            "LU" -> R.raw.luxemburg
            "LR" -> R.raw.liberia
            "MD" -> R.raw.moldova
            "MT" -> R.raw.malta
            "ML" -> R.raw.mali
            "MC" -> R.raw.monaco
            "ME" -> R.raw.montenegro
            "MN" -> R.raw.mongolia
            "MM" -> R.raw.myanmar
            "MR" -> R.raw.mauritania
            "MH" -> R.raw.marshall_islands
            "MK" -> R.raw.north_macedonia
            "MY" -> R.raw.malaysia
            "MX" -> R.raw.mexico
            "MA" -> R.raw.marocco
            "MG" -> R.raw.madagascar
            "MZ" -> R.raw.mozambique
            "NR" -> R.raw.nauru
            "NI" -> R.raw.nicaragua
            "NE" -> R.raw.niger
            "NL" -> R.raw.netherlands
            "NP" -> R.raw.nepal
            "NZ" -> R.raw.new_zealand
            "NO" -> R.raw.norway
            "NG" -> R.raw.nigeria
            "OM" -> R.raw.oman
            "PL" -> R.raw.poland
            "PT" -> R.raw.portugal
            "PW" -> R.raw.palau
            "PE" -> R.raw.peru
            "PK" -> R.raw.pakistan
            "PH" -> R.raw.philippines
            "PG" -> R.raw.papua
            "PY" -> R.raw.paraguay
            "PA" -> R.raw.panama
            "RO" -> R.raw.romania
            "RS" -> R.raw.serbia
            "RW" -> R.raw.rwanda
            "RU" -> R.raw.russia
            "SK" -> R.raw.slovakia
            "SI" -> R.raw.slovenia
            "SZ" -> R.raw.eswatini
            "SD" -> R.raw.sudan
            "SB" -> R.raw.solomon
            "SC" -> R.raw.seychelles
            "SY" -> R.raw.syria
            "SR" -> R.raw.suriname
            "SO" -> R.raw.somalia
            "SL" -> R.raw.sierra_leone
            "SN" -> R.raw.senegal
            "SS" -> R.raw.south_sudan
            "SE" -> R.raw.sweden
            "SV" -> R.raw.salvador
            "SG" -> R.raw.singapore
            "SM" -> R.raw.san_marino
            "ST" -> R.raw.sao_tome
            "SA" -> R.raw.saudi_arabia
            "TR" -> R.raw.turkey
            "TV" -> R.raw.tuvalu
            "TT" -> R.raw.trinidad
            "TZ" -> R.raw.tanzania
            "TG" -> R.raw.tongo
            "TO" -> R.raw.tonga
            "TH" -> R.raw.thailand
            "TM" -> R.raw.turkmenistan
            "TN" -> R.raw.tunisia
            "UA" -> R.raw.ukraine
            "UG" -> R.raw.uganda
            "UZ" -> R.raw.uzbekistan
            "UY" -> R.raw.uruguay
            "VA" -> R.raw.vatican
            "VU" -> R.raw.vanuatu
            "VE" -> R.raw.venezuela
            "VC" -> R.raw.saint_vincent
            "WS" -> R.raw.samoa
            "US" -> R.raw.united_states
            "YE" -> R.raw.yemen
            "ZA" -> R.raw.south_africa
            "ZW" -> R.raw.zimbabwe
            "ZM" -> R.raw.zambia

            else -> null
        }
    }
}