package com.peterlopusan.traveldiary.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.BottomNavigationBar
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.screens.countries.CountriesScreen
import com.peterlopusan.traveldiary.ui.screens.flights.FlightsScreen
import com.peterlopusan.traveldiary.ui.screens.map.MapScreen
import com.peterlopusan.traveldiary.ui.screens.places.PlacesScreen
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.utils.showLogs
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()

    val showSortDialogFlight = remember { mutableStateOf(false) }
    val showFilterSheetFlight = remember { mutableStateOf(false) }

    val showSortDialogPlace = remember { mutableStateOf(false) }
    val showFilterSheetPlace = remember { mutableStateOf(false) }

    val showSortDialogCountry = remember { mutableStateOf(false) }
    val showFilterSheetCountry = remember { mutableStateOf(false) }

    val filterButtonClick = if (pagerState.currentPage == 3) {
        null
    } else {
        { when (pagerState.currentPage) {
            0 -> { showFilterSheetFlight.value = !showFilterSheetFlight.value }
            1 -> { showFilterSheetPlace.value = !showFilterSheetPlace.value }
            2 -> { showFilterSheetCountry.value = !showFilterSheetCountry.value }
            else -> {}
        }}
    }

    val addButtonClick = if (pagerState.currentPage == 3) {
        null
    } else {
        {
            when (pagerState.currentPage) {
                0 -> {
                    MainActivity.navController.navigate(TravelDiaryRoutes.AddOrEditFlightScreen.name)
                }

                1 -> {
                    MainActivity.navController.navigate(TravelDiaryRoutes.AddOrEditPlaceScreen.name)
                }

                2 -> {
                    MainActivity.navController.navigate(TravelDiaryRoutes.AddOrEditCountryScreen.name)
                }

                else -> {}
            }
        }
    }

    val sortButtonClick = if (pagerState.currentPage == 3) {
        null
    } else {
        { when (pagerState.currentPage) {
            0 -> { showSortDialogFlight.value = !showSortDialogFlight.value }
            1 -> { showSortDialogPlace.value = !showSortDialogPlace.value }
            2 -> { showSortDialogCountry.value = !showSortDialogCountry.value }
            else -> {}
        }}
    }


    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            title = when (pagerState.currentPage) {
                0 -> MainActivity.instance.getString(R.string.bottom_navigation_bar_flights)
                1 -> MainActivity.instance.getString(R.string.bottom_navigation_bar_places)
                2 -> MainActivity.instance.getString(R.string.bottom_navigation_bar_countries)
                else -> MainActivity.instance.getString(R.string.bottom_navigation_bar_map)
            },
            filterButtonClick = filterButtonClick,
            addButtonClick = addButtonClick,
            sortButtonClick = sortButtonClick,
            showSettingsButton = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryBackground)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = pagerState.currentPage != 3,
                modifier = Modifier
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> FlightsScreen(showSortDialog = showSortDialogFlight, showFilterSheet = showFilterSheetFlight)
                    1 -> PlacesScreen(showSortDialog = showSortDialogPlace, showFilterSheet = showFilterSheetPlace)
                    2 -> CountriesScreen(showSortDialog = showSortDialogCountry, showFilterSheet = showFilterSheetCountry)
                    3 -> MapScreen()
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