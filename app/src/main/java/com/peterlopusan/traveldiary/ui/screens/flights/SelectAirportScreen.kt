package com.peterlopusan.traveldiary.ui.screens.flights

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.peterlopusan.traveldiary.models.flight.Airport
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.utils.openMap
import kotlinx.coroutines.launch

@Composable
fun SelectAirportScreen(navController: NavController) {
    val viewModel = MainActivity.flightViewModel
    val scope = rememberCoroutineScope()
    val airportList: SnapshotStateList<Airport> = remember { mutableStateListOf() }
    var searchText by remember { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            navController = navController,
            title = stringResource(id = R.string.select_airport_screen_toolbar_title),
            showBackButton = true
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .background(LocalTravelDiaryColors.current.primaryBackground)
                .padding(20.dp)
        ) {
            LazyColumn {
                itemsIndexed(airportList) { index, airport ->
                    SelectAirportItem(
                        modifier = Modifier
                            .clickable {
                                viewModel.setSelectedAirport(airport)
                                navController.popBackStack()
                            },
                        airport = airport,
                        index = index
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        Column(
            Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .background(LocalTravelDiaryColors.current.secondaryBackground)
                .padding(15.dp)

        ) {
            CustomTextField(
                hint = stringResource(id = R.string.select_airport_screen_search),
                text = searchText,
                borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                onValueChange = { searchText = it },
                icon = R.drawable.search_icon,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.select_airport_screen_search_button),
                clickAction = {
                    if (searchText.length > 2) {
                        showLoading = true
                        scope.launch {
                            airportList.clear()
                            airportList.addAll(viewModel.getAirportsList(searchText))
                            showLoading = false
                        }
                    }
                }
            )
        }
    }

    LoadingIndicator(showLoading)
}

@Composable
fun SelectAirportItem(modifier: Modifier, airport: Airport, index: Int) {
    var countryName by remember { mutableStateOf("") }
    val airportIcon = when(index%3) {
        0 -> R.drawable.airport1_icon
        1 -> R.drawable.airport2_icon
        else -> R.drawable.airport3_icon
    }

    LaunchedEffect(key1 = true) {
        val country = MainActivity.countryViewModel.getCountryFromShortname(airport.country)
        countryName = country?.name?.common ?: ""
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(airportIcon),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = airport.name ?: "",
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                text = stringResource(id = R.string.select_airport_screen_city, airport.city ?: ""),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.select_airport_screen_country, countryName),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.select_airport_screen_iata, airport.iata ?: ""),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )
        }

        Image(
            painter = painterResource(R.drawable.navigate_to_map_icon),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Bottom)
                .clickable {
                    if (airport.latitude != null && airport.longitude != null) {
                        openMap(airport.latitude.toDouble(), airport.longitude.toDouble(), 14)
                    }
                }
        )
    }
}