package com.peterlopusan.traveldiary.ui.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.ui.theme.MyCountryColor
import com.peterlopusan.traveldiary.ui.theme.VisitedCountryColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MapScreen () {
    val mapViewModel = MainActivity.mapViewModel
    val countryViewModel = MainActivity.countryViewModel
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(48.45, 16.42), 1f) }
    val myHomelandBorders = remember { mutableStateListOf<MutableList<MutableList<LatLng>>>() }
    val countryBordersList = remember { mutableStateListOf<MutableList<MutableList<LatLng>>>() }


    LaunchedEffect(key1 = true) {
        this.launch (Dispatchers.IO) {
            myHomelandBorders.add(mapViewModel.getMyHomelandCountryBorder(MainActivity.authViewModel.userInfo?.countryCode ?: ""))
            countryBordersList.add(mapViewModel.getCountriesBorder(countryViewModel.getVisitedCountriesList()))
        }
    }



    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {

        myHomelandBorders.forEach {
            it.forEach { points ->
                Polygon(
                    points = points,
                    clickable = true,
                    fillColor = MyCountryColor,
                    strokeColor = Color.Blue,
                    strokeWidth = 2f
                )
            }
        }

        countryBordersList.forEach {
            it.forEach { points ->
                Polygon(
                    points = points,
                    clickable = true,
                    fillColor = VisitedCountryColor,
                    strokeColor = Color.Blue,
                    strokeWidth = 2f
                )
            }
        }
    }
}
