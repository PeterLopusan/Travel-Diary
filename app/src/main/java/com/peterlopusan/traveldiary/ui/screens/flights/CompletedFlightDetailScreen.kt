package com.peterlopusan.traveldiary.ui.screens.flights

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.models.country.Country
import com.peterlopusan.traveldiary.models.flight.Airport
import com.peterlopusan.traveldiary.models.flight.CompletedFlight
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.ConfirmAlertDialog
import com.peterlopusan.traveldiary.ui.components.InfoCard
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.utils.openMap

@Composable
fun CompletedFlightDetailScreen(navController: NavController) {
    val viewModel = MainActivity.flightViewModel
    val completedFlight = viewModel.completedFlight1
    var resetValues by remember { mutableStateOf(true) }
    var showAlert by remember { mutableStateOf(false) }
    var durationText by remember { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        durationText = getDurationText(completedFlight)
    }

    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                viewModel.completedFlight1 = null
            }
        }
    }

    if (showAlert) {
        ConfirmAlertDialog(
            confirmClick = {
                showLoading = true
                showAlert = !showAlert
                viewModel.deleteFlight().observe(MainActivity.instance) {
                    if (it == true) {
                        navController.popBackStack()
                    }

                    showLoading = false
                }
            },
            cancelClick = {
                showAlert = !showAlert
            },
            dialogTitle = stringResource(id = R.string.completed_flight_detail_screen_delete_title),
            dialogText = stringResource(id = R.string.completed_flight_detail_screen_are_you_sure)
        )
    }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {

        Toolbar(
            navController = navController,
            title = stringResource(id = R.string.completed_flight_detail_screen_toolbar_title),
            showBackButton = true,
            editButtonClick = {
                resetValues = false
                navController.navigate(TravelDiaryRoutes.AddOrEditFlightScreen.name)
            },
            deleteButtonClick = {
                showAlert = !showAlert
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalTravelDiaryColors.current.primaryBackground)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.completed_flight_detail_screen_about_your_flight),
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
                    .background(LocalTravelDiaryColors.current.secondaryBackground)
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                InfoCard(
                    drawableImage = R.drawable.departure_icon,
                    title = stringResource(id = R.string.completed_flight_detail_screen_departure_airport),
                    text = completedFlight?.departureAirport?.name ?: ""
                )

                Spacer(modifier = Modifier.height(20.dp))

                InfoCard(
                    drawableImage = R.drawable.arrival_icon,
                    title = stringResource(id = R.string.completed_flight_detail_screen_arrival_airport),
                    text = completedFlight?.arrivalAirport?.name ?: ""
                )

                Spacer(modifier = Modifier.height(20.dp))

                InfoCard(
                    drawableImage = R.drawable.date_icon,
                    title = stringResource(id = R.string.completed_flight_detail_screen_flight_date),
                    text = completedFlight?.flightDate ?: ""
                )

                Spacer(modifier = Modifier.height(20.dp))

                InfoCard(
                    drawableImage = R.drawable.time_icon,
                    title = stringResource(id = R.string.completed_flight_detail_screen_flight_duration),
                    text = durationText
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(id = R.string.completed_flight_detail_screen_about_airports),
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            AirportInfoCard(viewModel.completedFlight1?.departureAirport, true)

            Spacer(modifier = Modifier.height(30.dp))

            AirportInfoCard(viewModel.completedFlight1?.arrivalAirport, false)

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    LoadingIndicator(showLoading = showLoading)
}

@Composable
fun AirportInfoCard(airport: Airport?, departure: Boolean) {
    var country by remember { mutableStateOf<Country?>(null) }

    LaunchedEffect(key1 = true) {
        country = MainActivity.countryViewModel.getCountryFromShortname(airport?.country)
    }


    Column(
        modifier = Modifier
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = if (departure) R.drawable.departure_icon else R.drawable.arrival_icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))

        InfoCard(
            drawableImage = R.drawable.airport1_icon,
            title = stringResource(id = R.string.completed_flight_detail_screen_airport_name),
            text = airport?.name ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.places_icon,
            title = stringResource(id = R.string.completed_flight_detail_screen_airport_city),
            text = airport?.city ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.subregion_icon,
            title = stringResource(id = R.string.completed_flight_detail_screen_airport_region),
            text = airport?.region ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            urlImage = country?.flags?.png,
            title = stringResource(id = R.string.completed_flight_detail_screen_airport_country),
            text = country?.name?.common ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.sea_level_icon,
            title = stringResource(id = R.string.completed_flight_detail_screen_airport_sea_level),
            text = stringResource(id = R.string.completed_flight_detail_screen_airport_sea_level_value, String.format("%.2f", getElevationInMeter(airport?.elevation)))
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.airport2_icon,
            title = stringResource(id = R.string.completed_flight_detail_screen_airport_iata),
            text = airport?.iata ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.airport3_icon,
            title = stringResource(id = R.string.completed_flight_detail_screen_airport_icao),
            text = airport?.icao ?: ""
        )

        Spacer(modifier = Modifier.height(15.dp))

        Image(
            painter = painterResource(id = R.drawable.navigate_to_map_icon),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    if (airport?.latitude != null && airport.longitude != null) {
                        try {
                            openMap(airport.latitude.toDouble(), airport.longitude.toDouble(), 14)
                        } catch (_: Exception) {
                        }
                    }
                }
        )
    }
}


private fun getDurationText(completedFlight: CompletedFlight?): String {
    return try {
        if (completedFlight?.durationHours?.toInt() == 1) {
            if (completedFlight.durationMinutes.toInt() == 1) {
                MainActivity.instance.getString(R.string.completed_flight_detail_screen_flight_duration_hour_minute, completedFlight.durationHours, completedFlight.durationMinutes)
            } else {
                MainActivity.instance.getString(R.string.completed_flight_detail_screen_flight_duration_hour_minutes, completedFlight.durationHours, completedFlight.durationMinutes)
            }
        } else {
            if (completedFlight?.durationMinutes?.toInt() == 1) {
                MainActivity.instance.getString(R.string.completed_flight_detail_screen_flight_duration_hours_minute, completedFlight.durationHours, completedFlight.durationMinutes)
            } else {
                MainActivity.instance.getString(R.string.completed_flight_detail_screen_flight_duration_hours_minutes, completedFlight?.durationHours ?: "", completedFlight?.durationMinutes ?: "")
            }
        }
    } catch (_: Exception) {
        ""
    }
}

private fun getElevationInMeter(elevation: Double?): Double {
    val meterConstant = 0.3048
    return (elevation ?: 0.0) * meterConstant
}