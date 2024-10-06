package com.peterlopusan.traveldiary.ui.screens.flights

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.enums.MainScreenEnums
import com.peterlopusan.traveldiary.models.flight.CompletedFlight
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.SortAlertDialog
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.utils.checkDates
import com.peterlopusan.traveldiary.utils.getCalendarFromString
import com.peterlopusan.traveldiary.utils.showDatePicker

@Composable
fun FlightsScreen(
    completedFlightsList: SnapshotStateList<CompletedFlight>,
    filteredFlightsList: SnapshotStateList<CompletedFlight>,
    showSortDialog: MutableState<Boolean>,
    showFilterSheet: MutableState<Boolean>,
    navController: NavController
) {
    val viewModel = MainActivity.flightViewModel
    val sharedPref = SharedPreferencesManager()

    /********************* FILTER *********************/
    val searchText = remember { mutableStateOf(sharedPref.getFlightSearch()) }
    val dateFrom = remember { mutableStateOf(sharedPref.getFlightDate(true)) }
    val dateTo = remember { mutableStateOf(sharedPref.getFlightDate(false)) }
    val durationFrom = remember { mutableStateOf(sharedPref.getFlightDuration(true)) }
    val durationTo = remember { mutableStateOf(sharedPref.getFlightDuration(false)) }


    if (showSortDialog.value) {
        SortAlertDialog(
            showFlightDuration = true,
            screenType = MainScreenEnums.FLIGHT,
            showName = false,
            closeClick = { showSortDialog.value = false },
            sortChanged = {
                viewModel.sortFlightList(filteredFlightsList)
            }
        )
    }

    if (showFilterSheet.value) {
        FlightFilterSheet(
            showFilterSheet = showFilterSheet,
            filteredFlightsList = filteredFlightsList,
            completedFlightsList = completedFlightsList,
            searchText = searchText,
            dateFrom = dateFrom,
            dateTo = dateTo,
            durationFrom = durationFrom,
            durationTo = durationTo
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .padding(horizontal = 20.dp)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = if (filteredFlightsList.size == 1) R.string.flights_screen_completed_flights_singular else R.string.flights_screen_completed_flights_plural,
                filteredFlightsList.size
            ),
            color = LocalTravelDiaryColors.current.primaryTextColor,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fonts
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn {
            itemsIndexed(filteredFlightsList) { _, completedFlight ->
                CompletedFlightItem(
                    modifier = Modifier
                        .clickable {
                            viewModel.completedFlight1 = completedFlight
                            navController.navigate(TravelDiaryRoutes.CompletedFlightDetailScreen.name)
                        },
                    completedFlight = completedFlight
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun CompletedFlightItem(
    modifier: Modifier = Modifier,
    completedFlight: CompletedFlight
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = completedFlight.flightDate,
            color = LocalTravelDiaryColors.current.primaryTextColor,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fonts,
                textDecoration = TextDecoration.Underline
            )
        )

        Row {
            Image(
                painter = painterResource(id = R.drawable.departure_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = completedFlight.departureAirport?.city ?: "",
                    color = LocalTravelDiaryColors.current.primaryTextColor,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Text(
                    text = completedFlight.departureAirport?.name ?: "",
                    color = LocalTravelDiaryColors.current.secondaryTextColor,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fonts
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Image(
                painter = painterResource(id = R.drawable.arrival_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = completedFlight.arrivalAirport?.city ?: "",
                    color = LocalTravelDiaryColors.current.primaryTextColor,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Text(
                    text = completedFlight.arrivalAirport?.name ?: "",
                    color = LocalTravelDiaryColors.current.secondaryTextColor,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fonts
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlightFilterSheet(
    showFilterSheet: MutableState<Boolean>,
    filteredFlightsList: SnapshotStateList<CompletedFlight>,
    completedFlightsList: SnapshotStateList<CompletedFlight>,
    searchText: MutableState<String>,
    dateFrom: MutableState<String>,
    dateTo: MutableState<String>,
    durationFrom: MutableState<String>,
    durationTo: MutableState<String>
) {
    val sheetState = rememberModalBottomSheetState()
    val sharedPref = SharedPreferencesManager()
    val viewModel = MainActivity.flightViewModel

    ModalBottomSheet(
        containerColor = LocalTravelDiaryColors.current.secondaryBackground,
        onDismissRequest = { showFilterSheet.value = false },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(bottom = 30.dp)
        ) {
            CustomTextField(
                hint = stringResource(id = R.string.search),
                text = searchText.value,
                borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                onValueChange = {
                    searchText.value = it
                    sharedPref.setFlightSearch(it)
                    viewModel.filterFlightList(
                        filteredFlightsList = filteredFlightsList,
                        completedFlightsList = completedFlightsList
                    )
                },
                icon = R.drawable.search_icon,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(LocalTravelDiaryColors.current.primaryTextColor)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.flights_screen_filter_date),
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row {
                CustomTextField(
                    hint = stringResource(id = R.string.flights_screen_filter_from),
                    text = dateFrom.value,
                    borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                    onValueChange = {},
                    icon = R.drawable.date_icon,
                    modifier = Modifier.weight(1f),
                    clickAction = {
                        showDatePicker(getCalendarFromString(dateFrom.value)) { day, month, year ->
                            dateFrom.value = "${day}.${month}.${year}"
                            checkDates(from = true, dateFrom = dateFrom, dateTo = dateTo)
                            sharedPref.setFlightDate(dateFrom.value, true)
                            viewModel.filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.width(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.flights_screen_filter_to),
                    text = dateTo.value,
                    borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                    onValueChange = {},
                    icon = R.drawable.date_icon,
                    modifier = Modifier.weight(1f),
                    clickAction = {
                        showDatePicker(getCalendarFromString(dateTo.value)) { day, month, year ->
                            dateTo.value = "${day}.${month}.${year}"
                            checkDates(from = false, dateFrom = dateFrom, dateTo = dateTo)
                            sharedPref.setFlightDate(dateTo.value, false)
                            viewModel.filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(LocalTravelDiaryColors.current.primaryTextColor)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.flights_screen_filter_flight_duration),
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row {
                CustomTextField(
                    inputType = KeyboardType.Number,
                    hint = stringResource(id = R.string.flights_screen_filter_from),
                    text = durationFrom.value,
                    borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                    onValueChange = {
                        var sendToFilter = false

                        if (it.length == 1 && it != "0") {
                            durationFrom.value = it
                            sendToFilter = true
                        } else {
                            if (it.length < 5) {
                                durationFrom.value = it
                                sendToFilter = true
                            }
                        }
                        if (sendToFilter) {
                            sharedPref.setFlightDuration(durationFrom.value, true)
                            viewModel.filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList
                            )
                        }
                    },
                    icon = R.drawable.time_icon,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(15.dp))

                CustomTextField(
                    inputType = KeyboardType.Number,
                    hint = stringResource(id = R.string.flights_screen_filter_to),
                    text = durationTo.value,
                    borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                    onValueChange = {
                        var sendToFilter = false
                        if (it.length == 1 && it != "0") {
                            durationTo.value = it
                            sendToFilter = true
                        } else {
                            if (it.length < 5) {
                                durationTo.value = it
                                sendToFilter = true
                            }
                        }

                        if (sendToFilter) {
                            sharedPref.setFlightDuration(durationTo.value, false)
                            viewModel.filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList
                            )
                        }
                    },
                    icon = R.drawable.time_icon,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.flights_screen_filter_reset_button),
                clickAction = {
                    sharedPref.resetFlightFilter()
                    searchText.value = ""
                    dateFrom.value = ""
                    dateTo.value = ""
                    durationFrom.value = ""
                    durationTo.value = ""
                    filteredFlightsList.clear()
                    filteredFlightsList.addAll(completedFlightsList)
                    viewModel.sortFlightList(filteredFlightsList)
                }
            )
        }
    }
}


