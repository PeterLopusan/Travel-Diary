package com.peterlopusan.traveldiary.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.models.country.VisitedCountry
import com.peterlopusan.traveldiary.models.flight.CompletedFlight
import com.peterlopusan.traveldiary.models.place.VisitedPlace
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.BottomNavigationBar
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.screens.countries.CountriesScreen
import com.peterlopusan.traveldiary.ui.screens.flights.FlightsScreen
import com.peterlopusan.traveldiary.ui.screens.map.MapScreen
import com.peterlopusan.traveldiary.ui.screens.places.PlacesScreen
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    var showLoading by remember { mutableStateOf(true) }

    /********************* FLIGHTS *********************/
    val completedFlightsList: SnapshotStateList<CompletedFlight> = remember { mutableStateListOf() }
    val filteredFlightsList: SnapshotStateList<CompletedFlight> = remember { mutableStateListOf() }
    val showSortDialogFlight = remember { mutableStateOf(false) }
    val showFilterSheetFlight = remember { mutableStateOf(false) }

    /********************* PLACES *********************/
    val visitedPlacesList: SnapshotStateList<VisitedPlace> = remember { mutableStateListOf() }
    val filteredPlacesList: SnapshotStateList<VisitedPlace> = remember { mutableStateListOf() }
    val showSortDialogPlace = remember { mutableStateOf(false) }
    val showFilterSheetPlace = remember { mutableStateOf(false) }

    /********************* COUNTRIES *********************/
    val visitedCountryList: SnapshotStateList<VisitedCountry> = remember { mutableStateListOf() }
    val filteredCountryList: SnapshotStateList<VisitedCountry> = remember { mutableStateListOf() }
    val showSortDialogCountry = remember { mutableStateOf(false) }
    val showFilterSheetCountry = remember { mutableStateOf(false) }

    /********************* MAP *********************/
    val showMapTypeDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        /********************* FLIGHTS *********************/
        completedFlightsList.addAll(MainActivity.flightViewModel.getCompletedFlights())
        filteredFlightsList.addAll(completedFlightsList)
        MainActivity.flightViewModel.filterFlightList(
            filteredFlightsList = filteredFlightsList,
            completedFlightsList = completedFlightsList
        )

        /********************* PLACES *********************/
        visitedPlacesList.addAll(MainActivity.placeViewModel.getVisitedPlaces())
        filteredPlacesList.addAll(visitedPlacesList)
        MainActivity.placeViewModel.filterPlaceList(
            filteredPlaceList = filteredPlacesList,
            visitedPlacesList = visitedPlacesList
        )

        /********************* COUNTRIES *********************/
        visitedCountryList.addAll(MainActivity.countryViewModel.getVisitedCountriesList())
        filteredCountryList.addAll(visitedCountryList)
        MainActivity.countryViewModel.filterCountryList(
            filteredCountryList = filteredCountryList,
            countryList = visitedCountryList
        )
        showLoading = false
    }

    val filterButtonClick = if (pagerState.currentPage == 3) {
        null
    } else {
        {
            when (pagerState.currentPage) {
                0 -> {
                    showFilterSheetFlight.value = !showFilterSheetFlight.value
                }

                1 -> {
                    showFilterSheetPlace.value = !showFilterSheetPlace.value
                }

                2 -> {
                    showFilterSheetCountry.value = !showFilterSheetCountry.value
                }

                else -> {}
            }
        }
    }

    val addButtonClick = if (pagerState.currentPage == 3) {
        null
    } else {
        {
            when (pagerState.currentPage) {
                0 -> {
                    navController.navigate(TravelDiaryRoutes.AddOrEditFlightScreen.name)
                }

                1 -> {
                    navController.navigate(TravelDiaryRoutes.AddOrEditPlaceScreen.name)
                }

                2 -> {
                    navController.navigate(TravelDiaryRoutes.AddOrEditCountryScreen.name)
                }

                else -> {}
            }
        }
    }

    val sortButtonClick = if (pagerState.currentPage == 3) {
        null
    } else {
        {
            when (pagerState.currentPage) {
                0 -> {
                    showSortDialogFlight.value = !showSortDialogFlight.value
                }

                1 -> {
                    showSortDialogPlace.value = !showSortDialogPlace.value
                }

                2 -> {
                    showSortDialogCountry.value = !showSortDialogCountry.value
                }

                else -> {}
            }
        }
    }

    val mapMarkerButtonClick = if (pagerState.currentPage != 3) {
        null
    } else {
        {
            showMapTypeDialog.value = !showMapTypeDialog.value
        }
    }


    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            navController = navController,
            title = when (pagerState.currentPage) {
                0 -> MainActivity.instance.getString(R.string.bottom_navigation_bar_flights)
                1 -> MainActivity.instance.getString(R.string.bottom_navigation_bar_places)
                2 -> MainActivity.instance.getString(R.string.bottom_navigation_bar_countries)
                else -> MainActivity.instance.getString(R.string.bottom_navigation_bar_map)
            },
            filterButtonClick = filterButtonClick,
            addButtonClick = addButtonClick,
            sortButtonClick = sortButtonClick,
            mapMarkerButtonClick = mapMarkerButtonClick,
            showSettingsButton = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalTravelDiaryColors.current.primaryBackground)
        ) {
            if (showLoading) {
                LoadingIndicator(showLoading = true)
            } else {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = pagerState.currentPage != 3,
                    modifier = Modifier
                        .weight(1f)
                ) { page ->
                    when (page) {
                        0 -> FlightsScreen(
                            completedFlightsList = completedFlightsList,
                            filteredFlightsList = filteredFlightsList,
                            showSortDialog = showSortDialogFlight,
                            showFilterSheet = showFilterSheetFlight,
                            navController = navController
                        )

                        1 -> PlacesScreen(
                            visitedPlacesList = visitedPlacesList,
                            showSortDialog = showSortDialogPlace,
                            filteredPlacesList = filteredPlacesList,
                            showFilterSheet = showFilterSheetPlace,
                            navController = navController
                        )

                        2 -> CountriesScreen(
                            visitedCountryList = visitedCountryList,
                            filteredCountryList = filteredCountryList,
                            showSortDialog = showSortDialogCountry,
                            showFilterSheet = showFilterSheetCountry,
                            navController = navController
                        )

                        3 -> MapScreen(showMapTypeDialog = showMapTypeDialog)
                    }
                }
            }

            BottomNavigationBar(
                activePage = pagerState.currentPage,
                itemClick = { page ->
                    scope.launch {
                        pagerState.scrollToPage(page)
                    }
                }
            )
        }
    }
}