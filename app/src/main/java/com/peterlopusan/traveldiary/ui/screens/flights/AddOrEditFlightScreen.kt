package com.peterlopusan.traveldiary.ui.screens.flights

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.data.enums.SelectAirportTypeEnum
import com.peterlopusan.traveldiary.data.models.flight.CompletedFlight
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomCheckbox
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.getCalendarFromString
import com.peterlopusan.traveldiary.utils.showDatePicker
import com.peterlopusan.traveldiary.utils.showLogs
import com.peterlopusan.traveldiary.utils.showToast

@Composable
fun AddOrEditFlightScreen() {
    val viewModel = MainActivity.flightViewModel
    var returnFlightShow = remember { mutableStateOf(viewModel.completedFlight2 != null) }
    var resetValues by remember { mutableStateOf(true) }
    val edit = viewModel.completedFlight1?.id != null
    var showLoading by remember { mutableStateOf(false) }


    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                viewModel.completedFlight1 = null
                viewModel.completedFlight2 = null
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            title = stringResource(id = if (edit) R.string.add_flight_screen_toolbar_edit_title else R.string.add_flight_screen_toolbar_add_title),
            showBackButton = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryBackground)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.secondaryBackground)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 15.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.flights_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(30.dp))

                FlightInfoCard(returnFlight = false) {
                    resetValues = false
                }

                Spacer(modifier = Modifier.height(15.dp))

                if (!edit) {
                    CustomCheckbox(
                        modifier = Modifier.align(Alignment.Start),
                        text = stringResource(id = R.string.add_flight_screen_flight_return_flight),
                        isChecked = returnFlightShow,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                viewModel.completedFlight2 = CompletedFlight(
                                    departureAirport = viewModel.completedFlight1?.arrivalAirport,
                                    arrivalAirport = viewModel.completedFlight1?.departureAirport,
                                    durationHours = viewModel.completedFlight1?.durationHours ?: "",
                                    durationMinutes = viewModel.completedFlight1?.durationMinutes ?: ""
                                )
                            } else {
                                viewModel.completedFlight2 = null
                            }
                            returnFlightShow.value = isChecked
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }

                if (returnFlightShow.value) {
                    FlightInfoCard(returnFlight = true) {
                        resetValues = false
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = if (edit) R.string.add_flight_screen_edit_button else R.string.add_flight_screen_create_button),
                    clickAction = {
                        if (checkDataBeforeSend()) {
                            showLoading = true
                            viewModel.createOrEditFlight().observe(MainActivity.instance) {
                                if (it == true) {
                                    if (edit) {
                                        resetValues = false
                                    }
                                    MainActivity.navController.popBackStack()
                                }
                                showLoading = false
                            }
                        }
                    }
                )
            }
        }
    }

    LoadingIndicator(showLoading)
}

@Composable
fun FlightInfoCard(
    returnFlight: Boolean,
    selectAirportAction: () -> Unit
) {
    val viewModel = MainActivity.flightViewModel
    val completedFlight = if (returnFlight) viewModel.completedFlight2 else viewModel.completedFlight1
    val departureAirport by remember { mutableStateOf(completedFlight?.departureAirport) }
    val arrivalAirport by remember { mutableStateOf(completedFlight?.arrivalAirport) }
    var flightDate by remember { mutableStateOf(completedFlight?.flightDate ?: "") }
    var durationHours by remember { mutableStateOf(completedFlight?.durationHours ?: "") }
    var durationMinutes by remember { mutableStateOf(completedFlight?.durationMinutes ?: "") }


    LaunchedEffect(key1 = true) {
        if (viewModel.completedFlight1 == null) {
            viewModel.completedFlight1 = CompletedFlight()
        }

        if (viewModel.completedFlight2 == null && returnFlight) {
            viewModel.completedFlight2 = CompletedFlight()
        }
    }

    CustomTextField(
        hint = stringResource(id = R.string.add_flight_screen_departure_airport),
        text = departureAirport?.name ?: "",
        onValueChange = {},
        clickAction = {
            selectAirportAction()
            viewModel.selectAirportType = if (returnFlight) SelectAirportTypeEnum.DEPARTURE_SECOND else SelectAirportTypeEnum.DEPARTURE_FIRST
            MainActivity.navController.navigate(TravelDiaryRoutes.SelectAirportScreen.name)
        },
        startIcon = R.drawable.departure_icon,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(15.dp))

    CustomTextField(
        hint = stringResource(id = R.string.add_flight_screen_arrival_airport),
        text = arrivalAirport?.name ?: "",
        onValueChange = {},
        clickAction = {
            selectAirportAction()
            viewModel.selectAirportType = if (returnFlight) SelectAirportTypeEnum.ARRIVAL_SECOND else SelectAirportTypeEnum.ARRIVAL_FIRST
            MainActivity.navController.navigate(TravelDiaryRoutes.SelectAirportScreen.name)
        },
        startIcon = R.drawable.arrival_icon,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(15.dp))

    CustomTextField(
        hint = stringResource(id = R.string.add_flight_screen_flight_date),
        text = flightDate,
        onValueChange = {},
        clickAction = {
            showDatePicker(getCalendarFromString(flightDate)) { day, month, year ->
                flightDate = "${day}.${month}.${year}"
                completedFlight?.flightDate = flightDate
            }
        },
        startIcon = R.drawable.date_icon,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(15.dp))

    Text(
        text = stringResource(id = R.string.add_flight_screen_flight_duration),
        color = MaterialTheme.colors.secondaryTextColor,
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontFamily = fonts
        )
    )
    Spacer(modifier = Modifier.height(5.dp))

    Row {
        CustomTextField(
            inputType = KeyboardType.Number,
            hint = stringResource(id = R.string.add_flight_screen_flight_hours),
            text = durationHours,
            onValueChange = {
                if (it.length < 3) {
                    durationHours = it
                    completedFlight?.durationHours = it
                }
            },
            startIcon = R.drawable.time_icon,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(15.dp))

        CustomTextField(
            inputType = KeyboardType.Number,
            hint = stringResource(id = R.string.add_flight_screen_flight_minutes),
            text = durationMinutes,
            onValueChange = {
                if (it.length == 1) {
                    if (it.toInt() < 6 && it != "0") {
                        durationMinutes = it
                        completedFlight?.durationMinutes = it
                    }
                } else {
                    if (it.length < 3) {
                        durationMinutes = it
                        completedFlight?.durationMinutes = it
                    }
                }
            },
            startIcon = R.drawable.time_icon,
            modifier = Modifier.weight(1f)
        )
    }
}

fun checkDataBeforeSend(): Boolean {
    val viewModel = MainActivity.flightViewModel

    return if (viewModel.completedFlight1?.departureAirport != null && viewModel.completedFlight1?.arrivalAirport != null && !viewModel.completedFlight1?.flightDate.isNullOrBlank()) {
        if (viewModel.completedFlight2 == null) {
            true
        } else if (viewModel.completedFlight2?.departureAirport != null && viewModel.completedFlight2?.arrivalAirport != null && !viewModel.completedFlight2?.flightDate.isNullOrBlank()) {
            true
        } else {
            showToast(MainActivity.instance.getString(R.string.add_flight_screen_missing_values))
            false
        }
    } else {
        showToast(MainActivity.instance.getString(R.string.add_flight_screen_missing_values))
        false
    }
}