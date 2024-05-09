package com.peterlopusan.traveldiary.ui.viewModels

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
import com.peterlopusan.traveldiary.api.RetrofitObject
import com.peterlopusan.traveldiary.countryList
import com.peterlopusan.traveldiary.data.models.country.Country
import com.peterlopusan.traveldiary.data.models.country.VisitedCountry
import com.peterlopusan.traveldiary.databaseUrl
import com.peterlopusan.traveldiary.utils.TranslateApiManager
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

    suspend fun getCountriesList(getFullList: Boolean = false): MutableList<Country> {

        if (allCountryList.isNotEmpty()) {
            return if (countryCodesForFiltering.isEmpty() || getFullList) {
                TranslateApiManager().translateCountryList(allCountryList).sortedBy { it.name?.common }.toMutableList()
            } else {
                var countryList = allCountryList

                countryCodesForFiltering.forEach { filterCountry ->
                    countryList = countryList.filterNot { it.shortname2 == filterCountry }.toMutableList()
                }

                TranslateApiManager().translateCountryList(countryList).sortedBy { it.name?.common }.toMutableList()
            }
        } else {
            try {
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
    }


    fun createOrEditCountryVisit(): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        val countryCode = selectedCountry?.shortname2 ?: ""
        visitedCountry?.countryCode = countryCode

        val countryInfo = visitedCountry?.countryInfo
        visitedCountry?.countryInfo = null

        database.child(auth.uid!!).child(countryList).child(countryCode).setValue(visitedCountry).addOnFailureListener {
            showLogs(it.message)
            liveData.value = false
        }.addOnSuccessListener {
            visitedCountry?.countryInfo = countryInfo
            liveData.value = true
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

    suspend fun getCountryFromShortname(shortname: String?): Country? {
        return try {
            val countryList = getCountriesList(getFullList = true)
            countryList.first { it.shortname2 == shortname }
        } catch (_: Exception) {
            null
        }
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
}