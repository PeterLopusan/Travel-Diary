package com.peterlopusan.traveldiary.ui.screens.places


import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import kotlinx.coroutines.launch

@Composable
fun GoogleMapsScreen(navController: NavController) {
    val viewModel = MainActivity.placeViewModel
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.HYBRID)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var latLng = LatLng(1.35, 103.87)
    var markerLatLng by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(latLng, 6f) }
    var searchText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var showLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        val zoomLevel: Float

        if (viewModel.selectedLatLng == null) {
            val countryShortname = MainActivity.authViewModel.userInfo?.countryCode
            val country = MainActivity.countryViewModel.getCountryFromShortname(countryShortname)
            country?.latlng?.let {
                if (country.latlng.size > 1) {
                    latLng = LatLng(country.latlng[0], country.latlng[1])
                }
            }
            zoomLevel = 4f
        } else {
            zoomLevel = 12f
            latLng = viewModel.selectedLatLng!!
            markerLatLng = latLng
        }

        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, zoomLevel, 0f, 0f)),
            durationMs = 500
        )

        showLoading = false
    }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)

        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                onMapClick = {
                    markerLatLng = it
                }
            ) {
                markerLatLng?.let {
                    Marker(
                        state = MarkerState(position = markerLatLng!!)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 30.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.search),
                    text = searchText,
                    onValueChange = {
                        searchText = it
                        if (it.length > 3) {
                            val searchedLatLng = getLatLonFromSearch(it)
                            searchedLatLng?.let {
                                scope.launch {
                                    latLng = searchedLatLng
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 12f, 0f, 0f)),
                                        durationMs = 1000
                                    )
                                }
                            }
                        }
                    },
                    icon = R.drawable.search_icon,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                CustomButton(
                    modifier = Modifier.width(200.dp),
                    text = stringResource(id = R.string.google_maps_screen_confirm),
                    clickAction = {
                        markerLatLng?.let {
                            viewModel.selectedLatLng = it
                            navController.popBackStack()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    LoadingIndicator(showLoading = showLoading, backgroundColor = LocalTravelDiaryColors.current.primaryBackground)

}


private fun getLatLonFromSearch(searchText: String): LatLng? {
    val geocoder = Geocoder(MainActivity.instance)

    return try {
        val addressList = geocoder.getFromLocationName(searchText, 1)
        LatLng(addressList?.get(0)!!.latitude, addressList[0]!!.longitude)
    } catch (_: Exception) {
        null
    }
}