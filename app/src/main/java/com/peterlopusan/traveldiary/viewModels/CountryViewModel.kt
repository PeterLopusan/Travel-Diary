package com.peterlopusan.traveldiary.viewModels

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.africa
import com.peterlopusan.traveldiary.antarctica
import com.peterlopusan.traveldiary.asia
import com.peterlopusan.traveldiary.countryList
import com.peterlopusan.traveldiary.databaseUrl
import com.peterlopusan.traveldiary.enums.ContinentsEnum
import com.peterlopusan.traveldiary.enums.SortTypeEnum
import com.peterlopusan.traveldiary.europe
import com.peterlopusan.traveldiary.models.country.Country
import com.peterlopusan.traveldiary.models.country.VisitedCountry
import com.peterlopusan.traveldiary.northAmerica
import com.peterlopusan.traveldiary.oceania
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.southAmerica
import com.peterlopusan.traveldiary.utils.TranslateApiManager
import com.peterlopusan.traveldiary.utils.formatDate
import com.peterlopusan.traveldiary.utils.normalizeAndLowercase
import com.peterlopusan.traveldiary.utils.showLogs
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CountryViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance(databaseUrl).reference
    private var auth: FirebaseAuth = Firebase.auth
    private var allCountryList = mutableListOf<Country>()

    var selectedCountry: Country? = null
    var countryCodesForFiltering = mutableListOf<String>()
    var visitedCountry: VisitedCountry? = null

    init {
        viewModelScope.launch {
            getCountriesList()
        }
    }

    fun getCountriesList(getFullList: Boolean = false): MutableList<Country> {
        try {
            /********************* Online ↓ *********************/
            /*
            val response = RetrofitObject.countryApi.getCountriesList()
            var countryList = listOf<Country>()

            if (countryCodesForFiltering.isEmpty() || getFullList) {
                response.body()?.let { countryList = it }
            } else {
                response.body()?.let {
                    countryList = response.body() ?: listOf()
                    countryCodesForFiltering.forEach { filterCountry ->
                        countryList = countryList.filterNot { it.shortname2 == filterCountry }
                    }
                }
            }
            */
            /********************* Offline ↓ *********************/
            val response = readCountryJsonFile()
            var countryList: List<Country>

            if (countryCodesForFiltering.isEmpty() || getFullList) {
                response.let { countryList = it }
            } else {
                response.let {
                    countryList = response
                    countryCodesForFiltering.forEach { filterCountry ->
                        countryList = countryList.filterNot { it.shortname2 == filterCountry }
                    }
                }
            }

            countryList.forEach {
                val originalName = it.name
                it.translations?.eng = originalName
            }

            allCountryList = countryList.toMutableList()

        } catch (ex: Exception) {
            allCountryList = readCountryJsonFile()
            showLogs(ex.message)
        }
        allCountryList = allCountryList.filter { it.independent == true }.toMutableList()
        return TranslateApiManager().translateCountryList(allCountryList).sortedBy { it.name?.common }.toMutableList()
    }


    fun createOrEditCountryVisit(): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        selectedCountry?.shortname2?.let {
            visitedCountry?.countryCode = it

            val countryInfo = visitedCountry?.countryInfo
            visitedCountry?.countryInfo = null

            database.child(auth.uid!!).child(countryList).child(it).setValue(visitedCountry).addOnFailureListener { exception ->
                showLogs(exception.message)
                liveData.value = false
            }.addOnSuccessListener {
                visitedCountry?.countryInfo = countryInfo
                liveData.value = true
            }
        }

        return liveData
    }

    fun deleteCountryVisit(): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        try {
            database.child(auth.uid!!).child(countryList).child(visitedCountry?.countryCode!!).removeValue().addOnFailureListener {
                showLogs(it.message)
                liveData.value = false
            }.addOnSuccessListener {
                liveData.value = true
                visitedCountry = null
            }
        } catch (e: Exception) {
            showLogs(e.message)
        }

        return liveData
    }

    suspend fun getVisitedCountriesList(): MutableList<VisitedCountry> {
        return suspendCoroutine { continuation ->
            val visitedCountryList = mutableListOf<VisitedCountry>()

            try {
                viewModelScope.launch {
                    val list = getCountriesList(true)

                    database.child(auth.uid!!).child(countryList).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dbChild in snapshot.children) {
                                val visitedCountry = dbChild.getValue(VisitedCountry::class.java)
                                val country = list.firstOrNull { it.shortname2 == visitedCountry?.countryCode }

                                visitedCountry?.countryInfo = country
                                visitedCountry?.let { visitedCountryList.add(it) }
                            }
                            visitedCountryList.reverse()
                            continuation.resume(visitedCountryList)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            continuation.resumeWithException(error.toException())
                        }
                    })
                }
            } catch (ex: Exception) {
                showLogs(ex.message)
                continuation.resumeWithException(ex)
            }
        }
    }

    fun getCountryFromShortname(shortname: String?): Country? {
        val countryList = getCountriesList(getFullList = true)
        return countryList.firstOrNull { it.shortname2 == shortname }
    }

    private fun readCountryJsonFile(): MutableList<Country> {
        return try {
            val inputStream = MainActivity.instance.resources.openRawResource(R.raw.country_list)
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()
            val countryListType = object : TypeToken<MutableList<Country>>() {}.type
            gson.fromJson(jsonText, countryListType)
        } catch (e: Exception) {
            showLogs(e.message)
            mutableListOf()
        }
    }

    fun filterCountryList(
        filteredCountryList: SnapshotStateList<VisitedCountry>,
        countryList: SnapshotStateList<VisitedCountry>
    ) {
        val sharedPref = SharedPreferencesManager()
        val searchText = sharedPref.getCountrySearch()
        val europeChecked = sharedPref.getIfContinentIsChecked(ContinentsEnum.EUROPE)
        val asiaChecked = sharedPref.getIfContinentIsChecked(ContinentsEnum.ASIA)
        val africaChecked = sharedPref.getIfContinentIsChecked(ContinentsEnum.AFRICA)
        val oceaniaChecked = sharedPref.getIfContinentIsChecked(ContinentsEnum.OCEANIA)
        val antarcticaChecked = sharedPref.getIfContinentIsChecked(ContinentsEnum.ANTARCTICA)
        val northAmericaChecked = sharedPref.getIfContinentIsChecked(ContinentsEnum.NORTH_AMERICA)
        val southAmericaChecked = sharedPref.getIfContinentIsChecked(ContinentsEnum.SOUTH_AMERICA)

        var list = mutableListOf<VisitedCountry>()
        list.addAll(countryList)

        if (searchText.isNotBlank()) {
            list = list.filter { it.countryInfo?.name?.common?.contains(searchText) == true || it.countryInfo?.name?.official?.contains(searchText) == true }.toMutableList()
        }

        if (!europeChecked) {
            list = list.filterNot { it.countryInfo?.continents?.contains(europe) == true }.toMutableList()
        }

        if (!asiaChecked) {
            list = list.filterNot { it.countryInfo?.continents?.contains(asia) == true }.toMutableList()
        }

        if (!africaChecked) {
            list = list.filterNot { it.countryInfo?.continents?.contains(africa) == true }.toMutableList()
        }

        if (!oceaniaChecked) {
            list = list.filterNot { it.countryInfo?.continents?.contains(oceania) == true }.toMutableList()
        }

        if (!antarcticaChecked) {
            list = list.filterNot { it.countryInfo?.continents?.contains(antarctica) == true }.toMutableList()
        }

        if (!northAmericaChecked) {
            list = list.filterNot { it.countryInfo?.continents?.contains(northAmerica) == true }.toMutableList()
        }

        if (!southAmericaChecked) {
            list = list.filterNot { it.countryInfo?.continents?.contains(southAmerica) == true }.toMutableList()
        }

        filteredCountryList.clear()
        filteredCountryList.addAll(list)
        sortCountryList(filteredCountryList)
    }

    fun sortCountryList(list: SnapshotStateList<VisitedCountry>) {
        list.apply {
            when (SharedPreferencesManager().getCountrySortPreference()) {
                SortTypeEnum.DATE_OLDEST_FIRST -> {
                    sortBy { formatDate(it.lastVisitDate, "dd.MM.yyyy") }
                }

                SortTypeEnum.DATE_NEWEST_FIRST -> {
                    sortBy { formatDate(it.lastVisitDate, "dd.MM.yyyy") }
                    reverse()
                }

                SortTypeEnum.NAME_ABC -> {
                    sortBy { normalizeAndLowercase(it.countryInfo?.name?.common) }
                }

                SortTypeEnum.NAME_ZYX -> {
                    sortBy { normalizeAndLowercase(it.countryInfo?.name?.common) }
                    reverse()
                }

                else -> {}
            }
        }

    }
}