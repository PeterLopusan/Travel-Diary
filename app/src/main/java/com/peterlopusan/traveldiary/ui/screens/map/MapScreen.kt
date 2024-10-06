package com.peterlopusan.traveldiary.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.enums.MapTypeEnum
import com.peterlopusan.traveldiary.models.flight.CompletedFlight
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomRadioButton
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.MyCountryColor
import com.peterlopusan.traveldiary.ui.theme.VisitedCountryColor
import com.peterlopusan.traveldiary.ui.theme.fonts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MapScreen(showMapTypeDialog: MutableState<Boolean>) {
    val selectedType = remember { mutableStateOf(SharedPreferencesManager().getMapTypePreference()) }

    if (showMapTypeDialog.value) {
        MapTypeDialog(
            showMapTypeDialog = showMapTypeDialog,
            selectedType = selectedType
        )
    }


    when(selectedType.value) {
        MapTypeEnum.FLIGHT_MAP -> FlightMap()
        MapTypeEnum.PLACE_MAP -> PlaceMap()
        MapTypeEnum.COUNTRY_MAP -> CountryMap()
    }
}


@Composable
private fun MapTypeDialog(
    modifier: Modifier = Modifier,
    showMapTypeDialog: MutableState<Boolean>,
    selectedType: MutableState<MapTypeEnum>
) {

    val flightMap = remember { mutableStateOf(selectedType.value == MapTypeEnum.FLIGHT_MAP) }
    val placeMap = remember { mutableStateOf(selectedType.value == MapTypeEnum.PLACE_MAP) }
    val countryMap = remember { mutableStateOf(selectedType.value == MapTypeEnum.COUNTRY_MAP) }


    Box(
        Modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Dialog(
            onDismissRequest = { showMapTypeDialog.value = !showMapTypeDialog.value }
        ) {
            Column(
                modifier = modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(LocalTravelDiaryColors.current.secondaryBackground)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.map_screen_map_type),
                    color = LocalTravelDiaryColors.current.primaryTextColor,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                CustomRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.map_screen_flight_map),
                    isSelect = flightMap,
                    clickAction = {
                        SharedPreferencesManager().setMapTypePreference(MapTypeEnum.FLIGHT_MAP)
                        placeMap.value = false
                        countryMap.value = false
                        selectedType.value = MapTypeEnum.FLIGHT_MAP
                    }
                )

                CustomRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.map_screen_place_map),
                    isSelect = placeMap,
                    clickAction = {
                        SharedPreferencesManager().setMapTypePreference(MapTypeEnum.PLACE_MAP)
                        flightMap.value = false
                        countryMap.value = false
                        selectedType.value = MapTypeEnum.PLACE_MAP
                    }
                )

                CustomRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.map_screen_country_map),
                    isSelect = countryMap,
                    clickAction = {
                        SharedPreferencesManager().setMapTypePreference(MapTypeEnum.COUNTRY_MAP)
                        flightMap.value = false
                        placeMap.value = false
                        selectedType.value = MapTypeEnum.COUNTRY_MAP
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                CustomButton(
                    clickAction = {
                        showMapTypeDialog.value = !showMapTypeDialog.value
                    },
                    text = stringResource(id = R.string.sort_alert_dialog_close_button)
                )
            }
        }
    }
}

@Composable
private fun FlightMap() {
    val flightViewModel = MainActivity.flightViewModel
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(48.45, 16.42), 1f) }
    val flightsList = remember { mutableStateListOf<CompletedFlight>() }

    LaunchedEffect(key1 = true) {
        this.launch(Dispatchers.IO) {
            flightsList.addAll(flightViewModel.getCompletedFlights())
        }

    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        flightsList.forEach { flight ->
            val departureLatLng = LatLng(flight.departureAirport?.latitude?.toDouble() ?: 0.0, flight.departureAirport?.longitude?.toDouble() ?: 0.0)
            val arrivalLatLng = LatLng(flight.arrivalAirport?.latitude?.toDouble() ?: 0.0, flight.arrivalAirport?.longitude?.toDouble() ?: 0.0)


            Marker(
                state = rememberMarkerState(position = departureLatLng),
                title = flight.departureAirport?.name,
                snippet = flight.departureAirport?.city
            )

            Marker(
                state = rememberMarkerState(position = arrivalLatLng),
                title = flight.arrivalAirport?.name,
                snippet = flight.arrivalAirport?.city
            )

            Polyline(
                points = listOf(departureLatLng,arrivalLatLng),
                color = Color.Red,
                width = 5f,
                clickable = true
            )
        }
    }
}

@Composable
private fun PlaceMap() {
    val mapViewModel = MainActivity.mapViewModel
    val placeViewModel = MainActivity.placeViewModel
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(48.45, 16.42), 1f) }
    val placesList = remember { mutableStateListOf<Pair<String, LatLng>>() }

    LaunchedEffect(key1 = true) {
        this.launch(Dispatchers.IO) {
            placesList.addAll(mapViewModel.getPlacesForMarkers(placeViewModel.getVisitedPlaces()))
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        placesList.forEach { place ->
            Marker(
                state = rememberMarkerState(position = place.second),
                title = place.first
            )
        }
    }
}


@Composable
private fun CountryMap() {
    val mapViewModel = MainActivity.mapViewModel
    val countryViewModel = MainActivity.countryViewModel
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(48.45, 16.42), 1f) }
    val myHomelandBorders = remember { mutableStateListOf<MutableList<MutableList<LatLng>>>() }
    val countryBordersList = remember { mutableStateListOf<MutableList<MutableList<LatLng>>>() }


    LaunchedEffect(key1 = true) {
        this.launch(Dispatchers.IO) {
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