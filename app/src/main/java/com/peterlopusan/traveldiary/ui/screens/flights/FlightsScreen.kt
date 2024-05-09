package com.peterlopusan.traveldiary.ui.screens.flights

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.data.enums.MainScreenEnums
import com.peterlopusan.traveldiary.data.enums.SortTypeEnum
import com.peterlopusan.traveldiary.data.models.flight.CompletedFlight
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.SortAlertDialog
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.checkDates
import com.peterlopusan.traveldiary.utils.formatDate
import com.peterlopusan.traveldiary.utils.getCalendarFromString
import com.peterlopusan.traveldiary.utils.showDatePicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FlightsScreen(showSortDialog: MutableState<Boolean>, showFilterSheet: MutableState<Boolean>) {
    val completedFlightsList: SnapshotStateList<CompletedFlight> = remember { mutableStateListOf() }
    val filteredFlightsList: SnapshotStateList<CompletedFlight> = remember { mutableStateListOf() }
    var showLoading by remember { mutableStateOf(true) }
    val viewModel = MainActivity.flightViewModel
    val sharedPref = SharedPreferencesManager()

    /********************* FILTER *********************/
    val searchText = remember { mutableStateOf(sharedPref.getFlightSearch()) }
    val dateFrom = remember { mutableStateOf(sharedPref.getFlightDate(true)) }
    val dateTo = remember { mutableStateOf(sharedPref.getFlightDate(false)) }
    val durationFrom = remember { mutableStateOf(sharedPref.getFlightDuration(true)) }
    val durationTo = remember { mutableStateOf(sharedPref.getFlightDuration(false)) }

    LaunchedEffect(key1 = true) {
        completedFlightsList.addAll(viewModel.getCompletedFlights())
        filteredFlightsList.addAll(completedFlightsList)
        filterFlightList(
            filteredFlightsList = filteredFlightsList,
            completedFlightsList = completedFlightsList,
            dateFrom = dateFrom.value,
            dateTo = dateTo.value,
            durationFrom = durationFrom.value,
            durationTo = durationTo.value,
            searchText = searchText.value.trimEnd()
        )
        showLoading = false
    }

    if (showSortDialog.value) {
        SortAlertDialog(
            showFlightDuration = true,
            screenType = MainScreenEnums.FLIGHT,
            showName = false,
            closeClick = { showSortDialog.value = false },
            sortChanged = {
                sortFlightList(filteredFlightsList)
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

    Box {
        if (showLoading) {
            LoadingIndicator(showLoading = true)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primaryBackground)
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        id = if (filteredFlightsList.size == 1) R.string.flights_screen_completed_flights_singular else R.string.flights_screen_completed_flights_plural,
                        filteredFlightsList.size
                    ),
                    color = MaterialTheme.colors.primaryTextColor,
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
                                    MainActivity.navController.navigate(TravelDiaryRoutes.CompletedFlightDetailScreen.name)
                                },
                            completedFlight = completedFlight
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
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
            .background(MaterialTheme.colors.secondaryBackground)
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = completedFlight.flightDate,
            color = MaterialTheme.colors.primaryTextColor,
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
                    color = MaterialTheme.colors.primaryTextColor,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Text(
                    text = completedFlight.departureAirport?.name ?: "",
                    color = MaterialTheme.colors.secondaryTextColor,
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
                    color = MaterialTheme.colors.primaryTextColor,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Text(
                    text = completedFlight.arrivalAirport?.name ?: "",
                    color = MaterialTheme.colors.secondaryTextColor,
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

    ModalBottomSheet(
        containerColor = MaterialTheme.colors.secondaryBackground,
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
                borderColor = MaterialTheme.colors.secondaryTextColor,
                onValueChange = {
                    searchText.value = it
                    sharedPref.setFlightSearch(it)
                    filterFlightList(
                        filteredFlightsList = filteredFlightsList,
                        completedFlightsList = completedFlightsList,
                        dateFrom = dateFrom.value,
                        dateTo = dateTo.value,
                        durationFrom = durationFrom.value,
                        durationTo = durationTo.value,
                        searchText = searchText.value.trimEnd()
                    )
                },
                startIcon = R.drawable.search_icon,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(MaterialTheme.colors.primaryTextColor)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.flights_screen_filter_date),
                color = MaterialTheme.colors.primaryTextColor,
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
                    borderColor = MaterialTheme.colors.secondaryTextColor,
                    onValueChange = {},
                    startIcon = R.drawable.date_icon,
                    modifier = Modifier.weight(1f),
                    clickAction = {
                        showDatePicker(getCalendarFromString(dateFrom.value)) { day, month, year ->
                            dateFrom.value = "${day}.${month}.${year}"
                            checkDates(from = true, dateFrom = dateFrom, dateTo = dateTo)
                            sharedPref.setFlightDate(dateFrom.value, true)
                            filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList,
                                dateFrom = dateFrom.value,
                                dateTo = dateTo.value,
                                durationFrom = durationFrom.value,
                                durationTo = durationTo.value,
                                searchText = searchText.value.trimEnd()
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.width(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.flights_screen_filter_to),
                    text = dateTo.value,
                    borderColor = MaterialTheme.colors.secondaryTextColor,
                    onValueChange = {},
                    startIcon = R.drawable.date_icon,
                    modifier = Modifier.weight(1f),
                    clickAction = {
                        showDatePicker(getCalendarFromString(dateTo.value)) { day, month, year ->
                            dateTo.value = "${day}.${month}.${year}"
                            checkDates(from = false, dateFrom = dateFrom, dateTo = dateTo)
                            sharedPref.setFlightDate(dateTo.value, false)
                            filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList,
                                dateFrom = dateFrom.value,
                                dateTo = dateTo.value,
                                durationFrom = durationFrom.value,
                                durationTo = durationTo.value,
                                searchText = searchText.value.trimEnd()
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(MaterialTheme.colors.primaryTextColor)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.flights_screen_filter_flight_duration),
                color = MaterialTheme.colors.primaryTextColor,
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
                    borderColor = MaterialTheme.colors.secondaryTextColor,
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
                            filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList,
                                dateFrom = dateFrom.value,
                                dateTo = dateTo.value,
                                durationFrom = durationFrom.value,
                                durationTo = durationTo.value,
                                searchText = searchText.value.trimEnd()
                            )
                        }
                    },
                    startIcon = R.drawable.time_icon,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(15.dp))

                CustomTextField(
                    inputType = KeyboardType.Number,
                    hint = stringResource(id = R.string.flights_screen_filter_to),
                    text = durationTo.value,
                    borderColor = MaterialTheme.colors.secondaryTextColor,
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
                            filterFlightList(
                                filteredFlightsList = filteredFlightsList,
                                completedFlightsList = completedFlightsList,
                                dateFrom = dateFrom.value,
                                dateTo = dateTo.value,
                                durationFrom = durationFrom.value,
                                durationTo = durationTo.value,
                                searchText = searchText.value.trimEnd()
                            )
                        }
                    },
                    startIcon = R.drawable.time_icon,
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
                    sortFlightList(filteredFlightsList)
                }
            )
        }
    }
}


private fun filterFlightList(
    filteredFlightsList: SnapshotStateList<CompletedFlight>,
    completedFlightsList: SnapshotStateList<CompletedFlight>,
    searchText: String,
    dateFrom: String,
    dateTo: String,
    durationFrom: String,
    durationTo: String
) {
    val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
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

private fun sortFlightList(list: SnapshotStateList<CompletedFlight>) {
    when (SharedPreferencesManager().getFlightSortPreference()) {
        SortTypeEnum.DATE_OLDEST_FIRST -> {
            list.sortBy { formatDate(it.flightDate, "dd.MM.yyyy") }

        }

        SortTypeEnum.DATE_NEWEST_FIRST -> {
            list.sortBy { formatDate(it.flightDate, "dd.MM.yyyy") }
            list.reverse()
        }

        SortTypeEnum.DURATION_LONGEST_FIRST -> {
            list.sortBy { getDurationInMinutes(it.durationHours, it.durationMinutes) }
            list.reverse()
        }

        SortTypeEnum.DURATION_SHORTEST_FIRST -> {
            list.sortBy { getDurationInMinutes(it.durationHours, it.durationMinutes) }
        }

        else -> {}
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