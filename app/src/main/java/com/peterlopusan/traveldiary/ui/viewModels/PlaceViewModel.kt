package com.peterlopusan.traveldiary.ui.viewModels

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.api.RetrofitObject
import com.peterlopusan.traveldiary.data.models.place.City
import com.peterlopusan.traveldiary.data.models.place.VisitedPlace
import com.peterlopusan.traveldiary.databaseUrl
import com.peterlopusan.traveldiary.flightList
import com.peterlopusan.traveldiary.placeList
import com.peterlopusan.traveldiary.storageUrl
import com.peterlopusan.traveldiary.utils.getBitmapFromUri
import com.peterlopusan.traveldiary.utils.getImageUri
import com.peterlopusan.traveldiary.utils.getResizedBitmap
import com.peterlopusan.traveldiary.utils.rotateBitmap
import com.peterlopusan.traveldiary.utils.showLogs
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val rangeConstant = 0.2

class PlaceViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance(databaseUrl).reference
    private var auth: FirebaseAuth = Firebase.auth
    private val storage = FirebaseStorage.getInstance(storageUrl).reference

    var selectedLatLng: LatLng? = null
    var visitedPlace: VisitedPlace? = null

    suspend fun getCitiesList(searchText: String): MutableList<City> {
        val citiesList = mutableListOf<City>()
        return try {
            val response: Response<MutableList<City>>? = if (selectedLatLng == null) {
                RetrofitObject.cityApi.getCitiesListByName(searchText.trimEnd())
            } else {
                val minLatitude = selectedLatLng?.latitude!! - rangeConstant
                val minLongitude = selectedLatLng?.longitude!! - rangeConstant
                val maxLatitude = selectedLatLng?.latitude!! + rangeConstant
                val maxLongitude = selectedLatLng?.longitude!! + rangeConstant
                RetrofitObject.cityApi.getCitiesListByNameLatLon(searchText.trimEnd(), minLatitude.toString(), minLongitude.toString(), maxLatitude.toString(), maxLongitude.toString())
            }
            response?.body()?.let {
                citiesList.addAll(it)
            }
            citiesList
        } catch (ex: Exception) {
            showLogs(ex.message)
            citiesList
        }
    }

    fun createOrEditPlaceVisit(): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        val key = visitedPlace?.id ?: System.currentTimeMillis().toString()
        visitedPlace?.id = key


        if (visitedPlace?.imageUrl == null || visitedPlace?.imageUrl?.contains("https") == true) {
            database.child(auth.uid!!).child(placeList).child(key).setValue(visitedPlace).addOnFailureListener {
                showLogs(it.message)
                liveData.value = false
            }.addOnSuccessListener {
                liveData.value = true
            }
        } else {
            val inputStream = MainActivity.instance.contentResolver.openInputStream(visitedPlace?.imageUrl!!.toUri())
            val bitmap = getBitmapFromUri(visitedPlace?.imageUrl!!.toUri())

            bitmap?.let {
                val rotatedBitmap = rotateBitmap(bitmap, inputStream!!)
                val resizedBitmap = getResizedBitmap(rotatedBitmap)
                val uri = getImageUri(resizedBitmap)
                uri?.let {

                    storage.child(auth.uid!!).child(key).putFile(uri).addOnCompleteListener {
                        storage.child(auth.uid!!).child(key).downloadUrl.addOnCompleteListener { storageUri ->
                            visitedPlace?.imageUrl = storageUri.result.toString()
                            database.child(auth.uid!!).child(placeList).child(key).setValue(visitedPlace).addOnFailureListener {
                                showLogs(it.message)
                                liveData.value = false
                            }.addOnSuccessListener {
                                liveData.value = true
                            }
                        }
                    }
                }
            }
            inputStream?.close()
        }

        return liveData
    }


    suspend fun getVisitedPlaces(): MutableList<VisitedPlace> {
        return suspendCoroutine { continuation ->
            val visitedPlacesList = mutableListOf<VisitedPlace>()

            try {
                database.child(auth.uid!!).child(placeList).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dbChild in snapshot.children) {
                            val completedFlight = dbChild.getValue(VisitedPlace::class.java)
                            completedFlight?.let { visitedPlacesList.add(it) }
                        }
                        visitedPlacesList.reverse()
                        continuation.resume(visitedPlacesList)
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

    fun deleteVisit(): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        try {
            database.child(auth.uid!!).child(placeList).child(visitedPlace?.id!!).removeValue().addOnFailureListener {
                showLogs(it.message)
                liveData.value = false
            }.addOnSuccessListener {
                if (visitedPlace?.imageUrl == null) {
                    liveData.value = true
                    visitedPlace = null
                } else {
                    storage.child(auth.uid!!).child(visitedPlace?.id!!).delete().addOnSuccessListener {
                        liveData.value = true
                        visitedPlace = null
                    }
                }
            }
        } catch (e: Exception) {
            showLogs(e.message)
        }

        return liveData
    }
}