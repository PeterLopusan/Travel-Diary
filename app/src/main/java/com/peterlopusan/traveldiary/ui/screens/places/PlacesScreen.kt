package com.peterlopusan.traveldiary.ui.screens.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.enums.MainScreenEnums
import com.peterlopusan.traveldiary.models.place.VisitedPlace
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomCheckbox
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.SortAlertDialog
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.utils.checkDates
import com.peterlopusan.traveldiary.utils.getCalendarFromString
import com.peterlopusan.traveldiary.utils.showDatePicker

@Composable
fun PlacesScreen(
    visitedPlacesList: SnapshotStateList<VisitedPlace>,
    filteredPlacesList: SnapshotStateList<VisitedPlace>,
    showSortDialog: MutableState<Boolean>,
    showFilterSheet: MutableState<Boolean>,
    navController: NavController
) {

    val viewModel = MainActivity.placeViewModel
    val sharedPref = SharedPreferencesManager()

    /********************* FILTER *********************/
    val searchText = remember { mutableStateOf(sharedPref.getPlaceSearch()) }
    val dateFrom = remember { mutableStateOf(sharedPref.getPlaceDate(true)) }
    val dateTo = remember { mutableStateOf(sharedPref.getPlaceDate(false)) }
    val isCityChecked = remember { mutableStateOf(sharedPref.getIfPlaceCityIsChecked(true)) }
    val isPlaceChecked = remember { mutableStateOf(sharedPref.getIfPlaceCityIsChecked(false)) }



    if (showSortDialog.value) {
        SortAlertDialog(
            showFlightDuration = false,
            screenType = MainScreenEnums.PLACE,
            showName = true,
            closeClick = { showSortDialog.value = false },
            sortChanged = {
                viewModel.sortPlaceList(filteredPlacesList)
            }
        )
    }

    if (showFilterSheet.value) {
        PlaceFilterSheet(
            showFilterSheet = showFilterSheet,
            filteredVisitedPlacesList = filteredPlacesList,
            visitedPlacesList = visitedPlacesList,
            searchText = searchText,
            dateFrom = dateFrom,
            dateTo = dateTo,
            isCityChecked = isCityChecked,
            isPlaceChecked = isPlaceChecked
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
            text = stringResource(id = R.string.places_screen_visited_places_plural, filteredPlacesList.size),
            color = LocalTravelDiaryColors.current.primaryTextColor,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fonts
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn {
            itemsIndexed(filteredPlacesList) { _, visitedPlace ->
                if (visitedPlace.visitedCity != null) {
                    VisitedCityItem(
                        modifier = Modifier
                            .clickable {
                                viewModel.visitedPlace = visitedPlace
                                navController.navigate(TravelDiaryRoutes.VisitedPlaceDetailScreen.name)
                            },
                        visitedPlace = visitedPlace
                    )
                } else if (visitedPlace.visitedPlace != null) {
                    VisitedPlaceItem(
                        modifier = Modifier
                            .clickable {
                                viewModel.visitedPlace = visitedPlace
                                navController.navigate(TravelDiaryRoutes.VisitedPlaceDetailScreen.name)
                            },
                        visitedPlace = visitedPlace
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun VisitedCityItem(
    modifier: Modifier = Modifier,
    visitedPlace: VisitedPlace
) {
    var countryName by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        countryName = MainActivity.countryViewModel.getCountryFromShortname(visitedPlace.visitedCity?.country)?.name?.common ?: ""
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        if (visitedPlace.imageUrl == null) {
            Image(
                painter = painterResource(id = R.drawable.city_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10))
                    .border(2.dp, Color.Gray, RoundedCornerShape(10))
                    .align(Alignment.CenterVertically)
                    .background(Color.White)
            )
        } else {
            AsyncImage(
                model = visitedPlace.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10))
                    .border(2.dp, Color.Gray, RoundedCornerShape(10))
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(
                text = visitedPlace.visitedCity?.name ?: "",
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                text = stringResource(id = R.string.places_screen_country, countryName),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.places_screen_last_visit_date, visitedPlace.lastVisitDate ?: ""),
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


@Composable
fun VisitedPlaceItem(
    modifier: Modifier = Modifier,
    visitedPlace: VisitedPlace
) {
    var countryName by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        countryName = MainActivity.countryViewModel.getCountryFromShortname(visitedPlace.visitedPlace?.country)?.name?.common ?: ""
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        if (visitedPlace.imageUrl == null) {
            Image(
                painter = painterResource(id = R.drawable.place_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10))
                    .border(2.dp, Color.Gray, RoundedCornerShape(10))
                    .align(Alignment.CenterVertically)
                    .background(Color.White)
            )
        } else {
            AsyncImage(
                model = visitedPlace.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10))
                    .border(2.dp, Color.Gray, RoundedCornerShape(10))
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(
                text = visitedPlace.visitedPlace?.name ?: "",
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                text = stringResource(id = R.string.places_screen_country, countryName),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.places_screen_last_visit_date, visitedPlace.lastVisitDate ?: ""),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceFilterSheet(
    showFilterSheet: MutableState<Boolean>,
    filteredVisitedPlacesList: SnapshotStateList<VisitedPlace>,
    visitedPlacesList: SnapshotStateList<VisitedPlace>,
    searchText: MutableState<String>,
    dateFrom: MutableState<String>,
    dateTo: MutableState<String>,
    isCityChecked: MutableState<Boolean>,
    isPlaceChecked: MutableState<Boolean>
) {
    val sheetState = rememberModalBottomSheetState()
    val sharedPref = SharedPreferencesManager()
    val viewModel = MainActivity.placeViewModel

    ModalBottomSheet(
        containerColor = LocalTravelDiaryColors.current.secondaryBackground,
        onDismissRequest = {
            showFilterSheet.value = false
        },
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
                    sharedPref.setPlaceSearch(it)
                    viewModel.filterPlaceList(
                        filteredPlaceList = filteredVisitedPlacesList,
                        visitedPlacesList = visitedPlacesList
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
                text = stringResource(id = R.string.places_screen_filter_date),
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
                    hint = stringResource(id = R.string.places_screen_filter_from),
                    text = dateFrom.value,
                    borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                    onValueChange = {},
                    icon = R.drawable.date_icon,
                    modifier = Modifier.weight(1f),
                    clickAction = {
                        showDatePicker(getCalendarFromString(dateFrom.value)) { day, month, year ->
                            dateFrom.value = "${day}.${month}.${year}"
                            checkDates(from = true, dateFrom = dateFrom, dateTo = dateTo)
                            sharedPref.setPlaceDate(dateFrom.value, true)
                            viewModel.filterPlaceList(
                                filteredPlaceList = filteredVisitedPlacesList,
                                visitedPlacesList = visitedPlacesList
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.width(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.places_screen_filter_to),
                    text = dateTo.value,
                    borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                    onValueChange = {},
                    icon = R.drawable.date_icon,
                    modifier = Modifier.weight(1f),
                    clickAction = {
                        showDatePicker(getCalendarFromString(dateTo.value)) { day, month, year ->
                            dateTo.value = "${day}.${month}.${year}"
                            checkDates(from = false, dateFrom = dateFrom, dateTo = dateTo)
                            sharedPref.setPlaceDate(dateFrom.value, false)
                            viewModel.filterPlaceList(
                                filteredPlaceList = filteredVisitedPlacesList,
                                visitedPlacesList = visitedPlacesList
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

            CustomCheckbox(
                text = stringResource(id = R.string.places_screen_filter_cities),
                isChecked = isCityChecked,
                onCheckedChange = { isChecked ->
                    isCityChecked.value = isChecked
                    sharedPref.setIfPlaceCityIsChecked(isChecked = isChecked, city = true)
                    viewModel.filterPlaceList(
                        filteredPlaceList = filteredVisitedPlacesList,
                        visitedPlacesList = visitedPlacesList
                    )
                }
            )

            CustomCheckbox(
                text = stringResource(id = R.string.places_screen_filter_places),
                isChecked = isPlaceChecked,
                onCheckedChange = { isChecked ->
                    isPlaceChecked.value = isChecked
                    sharedPref.setIfPlaceCityIsChecked(isChecked = isChecked, city = false)
                    viewModel.filterPlaceList(
                        filteredPlaceList = filteredVisitedPlacesList,
                        visitedPlacesList = visitedPlacesList
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.flights_screen_filter_reset_button),
                clickAction = {
                    sharedPref.resetPlaceFilter()
                    searchText.value = ""
                    dateFrom.value = ""
                    dateTo.value = ""
                    isCityChecked.value = true
                    isPlaceChecked.value = true
                    filteredVisitedPlacesList.clear()
                    filteredVisitedPlacesList.addAll(visitedPlacesList)
                    viewModel.sortPlaceList(filteredVisitedPlacesList)
                }
            )
        }
    }
}

