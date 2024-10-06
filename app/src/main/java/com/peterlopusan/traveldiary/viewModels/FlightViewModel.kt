package com.peterlopusan.traveldiary.viewModels

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.peterlopusan.traveldiary.api.RetrofitObject
import com.peterlopusan.traveldiary.databaseUrl
import com.peterlopusan.traveldiary.enums.SelectAirportTypeEnum
import com.peterlopusan.traveldiary.enums.SortTypeEnum
import com.peterlopusan.traveldiary.flightList
import com.peterlopusan.traveldiary.models.flight.Airport
import com.peterlopusan.traveldiary.models.flight.CompletedFlight
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.utils.formatDate
import com.peterlopusan.traveldiary.utils.showLogs
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FlightViewModel: ViewModel() {
    private val database = FirebaseDatabase.getInstance(databaseUrl).reference
    private var auth: FirebaseAuth =  Firebase.auth

    var selectAirportType = SelectAirportTypeEnum.DEPARTURE_FIRST
    var completedFlight1: CompletedFlight? = null
    var completedFlight2: CompletedFlight? = null


    suspend fun getAirportsList(searchedText: String): MutableList<Airport> {
        val airportList = mutableListOf<Airport>()

        return try {
            airportList.addAll(RetrofitObject.airportApi.getAirportsListByName(searchedText.trimEnd())?.body() ?: mutableListOf())
            airportList.addAll(RetrofitObject.airportApi.getAirportsListByCity(searchedText.trimEnd())?.body() ?: mutableListOf())
            airportList.addAll(RetrofitObject.airportApi.getAirportsListByIata(searchedText.trimEnd())?.body() ?: mutableListOf())

            airportList.distinct().filter { !it.iata.isNullOrBlank() }.toMutableList()
        } catch (ex: Exception) {
            showLogs(ex.message)
            airportList
        }
    }

    fun setSelectedAirport(airport: Airport) {
        when(selectAirportType) {
            SelectAirportTypeEnum.DEPARTURE_FIRST -> completedFlight1?.departureAirport = airport
            SelectAirportTypeEnum.ARRIVAL_FIRST -> completedFlight1?.arrivalAirport = airport
            SelectAirportTypeEnum.DEPARTURE_SECOND -> completedFlight2?.departureAirport = airport
            SelectAirportTypeEnum.ARRIVAL_SECOND -> completedFlight2?.arrivalAirport = airport
        }
    }


    fun createOrEditFlight(): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        var key = completedFlight1?.id ?: System.currentTimeMillis().toString()

        completedFlight1?.id = key

        database.child(auth.uid!!).child(flightList).child(key).setValue(completedFlight1).addOnFailureListener {
            showLogs(it.message)
            liveData.value = false
        }.addOnSuccessListener {
            if (completedFlight2 == null) {
                liveData.value = true
            } else {
                key = System.currentTimeMillis().toString()
                completedFlight2?.id = key
                database.child(auth.uid!!).child(flightList).child(key).setValue(completedFlight2).addOnFailureListener {
                    showLogs(it.message)
                    liveData.value = false
                }.addOnSuccessListener {
                    liveData.value = true
                }
            }
        }

        return liveData
    }

    suspend fun getCompletedFlights(): MutableList<CompletedFlight> {
        return suspendCoroutine { continuation ->
            val completedFlightsList = mutableListOf<CompletedFlight>()

            try {
                database.child(auth.uid!!).child(flightList).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dbChild in snapshot.children) {
                            val completedFlight = dbChild.getValue(CompletedFlight::class.java)
                            completedFlight?.let { completedFlightsList.add(it) }
                        }

                        continuation.resume(completedFlightsList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                })
            } catch (ex: Exception) {
                showLogs(ex.message)
                continuation.resumeWithException(ex)
            }
        }
    }

    fun deleteFlight(): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        try {
            database.child(auth.uid!!).child(flightList).child(completedFlight1?.id!!).removeValue().addOnFailureListener {
                showLogs(it.message)
                liveData.value = false
            }.addOnSuccessListener {
                liveData.value = true
                completedFlight1 = null
            }
        } catch (e: Exception) {
            showLogs(e.message)
        }

        return liveData
    }

    fun filterFlightList(
        filteredFlightsList: SnapshotStateList<CompletedFlight>,
        completedFlightsList: SnapshotStateList<CompletedFlight>
    ) {
        val sharedPref = SharedPreferencesManager()
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val  searchText = sharedPref.getFlightSearch()
        val dateFrom = sharedPref.getFlightDate(true)
        val dateTo = sharedPref.getFlightDate(false)
        val durationFrom = sharedPref.getFlightDuration(true)
        val durationTo = sharedPref.getFlightDuration(false)

        var list = mutableListOf<CompletedFlight>()
        list.addAll(completedFlightsList)

        if (searchText.isNotBlank()) {
            list = list.filter {
                it.arrivalAirport?.city?.contains(searchText) == true ||
                        it.arrivalAirport?.name?.contains(searchText) == true ||
                        it.departureAirport?.city?.contains(searchText) == true ||
                        it.departureAirport?.name?.contains(searchText) == true
            }.toMutableList()
        }

        if (dateFrom.isNotBlank()) {
            val date = LocalDate.parse(dateFrom, format)
            list = list.filter { LocalDate.parse(it.flightDate, format).isAfter(date) || LocalDate.parse(it.flightDate, format) == date }.toMutableList()
        }

        if (dateTo.isNotBlank()) {
            val date = LocalDate.parse(dateTo, format)
            list = list.filter { LocalDate.parse(it.flightDate, format).isBefore(date) || LocalDate.parse(it.flightDate, format) == date }.toMutableList()
        }

        if (durationFrom.isNotBlank()) {
            list = list.filter { getDurationInMinutes(it.durationHours, it.durationMinutes) >= durationFrom.toInt() }.toMutableList()
        }

        if (durationTo.isNotBlank()) {
            list = list.filter { getDurationInMinutes(it.durationHours, it.durationMinutes) <= durationTo.toInt() }.toMutableList()
        }

        filteredFlightsList.clear()
        filteredFlightsList.addAll(list)
        sortFlightList(filteredFlightsList)
    }

    fun sortFlightList(list: SnapshotStateList<CompletedFlight>) {
        list.apply {
            when (SharedPreferencesManager().getFlightSortPreference()) {
                SortTypeEnum.DATE_OLDEST_FIRST -> {
                    sortBy { formatDate(it.flightDate, "dd.MM.yyyy") }
                }

                SortTypeEnum.DATE_NEWEST_FIRST -> {
                    sortBy { formatDate(it.flightDate, "dd.MM.yyyy") }
                    reverse()
                }

                SortTypeEnum.DURATION_LONGEST_FIRST -> {
                    sortBy { getDurationInMinutes(it.durationHours, it.durationMinutes) }
                    reverse()
                }

                SortTypeEnum.DURATION_SHORTEST_FIRST -> {
                    sortBy { getDurationInMinutes(it.durationHours, it.durationMinutes) }
                }

                else -> {}
            }
        }
    }

    private fun getDurationInMinutes(hours: String, minutes: String): Int {
        val hoursInteger = try {
            hours.toInt()
        } catch (_: Exception) {
            0
        }
        val minutesInteger = try {
            minutes.toInt()
        } catch (_: Exception) {
            0
        }

        return (hoursInteger * 60) + minutesInteger
    }
}