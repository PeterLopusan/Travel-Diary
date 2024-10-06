package com.peterlopusan.traveldiary.ui.screens.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.peterlopusan.traveldiary.models.country.Country
import com.peterlopusan.traveldiary.models.place.City
import com.peterlopusan.traveldiary.models.place.Place
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.ConfirmAlertDialog
import com.peterlopusan.traveldiary.ui.components.InfoCard
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.utils.formatNumberWithSpaces
import com.peterlopusan.traveldiary.utils.openMap

@Composable
fun VisitedPlaceDetailScreen(navController: NavController) {
    val viewModel = MainActivity.placeViewModel
    var resetValues by remember { mutableStateOf(true) }
    var showAlert by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }


    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                viewModel.visitedPlace = null
                viewModel.selectedLatLng = null
            }
        }
    }

    if (showAlert) {
        ConfirmAlertDialog(
            confirmClick = {
                showLoading = true
                showAlert = !showAlert
                viewModel.deleteVisit().observe(MainActivity.instance) {
                    if (it == true) {
                        navController.popBackStack()
                    }
                    showLoading = false
                }
            },
            cancelClick = {
                showAlert = !showAlert
            },
            dialogTitle = stringResource(id = R.string.visited_place_detail_screen_delete_title),
            dialogText = stringResource(id = R.string.visited_place_detail_screen_are_you_sure)
        )
    }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {

        Toolbar(
            navController = navController,
            title = stringResource(id = R.string.visited_place_detail_screen_toolbar_title),
            showBackButton = true,
            editButtonClick = {
                resetValues = false
                navController.navigate(TravelDiaryRoutes.AddOrEditPlaceScreen.name)
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
                text = stringResource(id = R.string.visited_place_detail_screen_about_your_visit),
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
                if (viewModel.visitedPlace?.imageUrl == null) {
                    Image(
                        painter = painterResource(id = if (viewModel.visitedPlace?.visitedCity != null) R.drawable.city_icon else R.drawable.place_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    AsyncImage(
                        model = viewModel.visitedPlace?.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10))
                            .border(2.dp, Color.Gray, RoundedCornerShape(10))
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                InfoCard(
                    drawableImage = R.drawable.note_icon,
                    title = stringResource(id = R.string.visited_place_detail_screen_note),
                    text = viewModel.visitedPlace?.note ?: ""
                )

                Spacer(modifier = Modifier.height(20.dp))

                InfoCard(
                    drawableImage = R.drawable.date_icon,
                    title = stringResource(id = R.string.visited_place_detail_screen_last_visit_date),
                    text = viewModel.visitedPlace?.lastVisitDate ?: ""
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(id = if (viewModel.visitedPlace?.visitedCity != null) R.string.visited_place_detail_screen_about_city else R.string.visited_place_detail_screen_about_place),
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (viewModel.visitedPlace?.visitedCity != null) {
                CityInfoCard(viewModel.visitedPlace?.visitedCity!!)
            } else if (viewModel.visitedPlace?.visitedPlace != null) {
                PlaceInfoCard(viewModel.visitedPlace?.visitedPlace!!)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    LoadingIndicator(showLoading = showLoading)
}

@Composable
fun CityInfoCard(city: City) {
    var country by remember { mutableStateOf<Country?>(null) }

    LaunchedEffect(key1 = true) {
        country = MainActivity.countryViewModel.getCountryFromShortname(city.country)
    }

    Column(
        modifier = Modifier
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        InfoCard(
            drawableImage = R.drawable.city_icon,
            title = stringResource(id = R.string.visited_place_detail_screen_name),
            text = city.name ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.countries_icon,
            title = stringResource(id = R.string.visited_place_detail_screen_country),
            text = country?.name?.common ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.population_icon,
            title = stringResource(id = R.string.visited_place_detail_screen_population),
            text = formatNumberWithSpaces(city.population?.toLong())
        )

        Spacer(modifier = Modifier.height(15.dp))

        Image(
            painter = painterResource(id = R.drawable.navigate_to_map_icon),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    if (city.latitude != null && city.longitude != null) {
                        try {
                            openMap(city.latitude!!.toDouble(), city.longitude!!.toDouble(), 14)
                        } catch (_: Exception) {}
                    }
                }
        )
    }
}

@Composable
fun PlaceInfoCard(place: Place) {
    var country by remember { mutableStateOf<Country?>(null) }

    LaunchedEffect(key1 = true) {
        country = MainActivity.countryViewModel.getCountryFromShortname(place.country)
    }

    Column(
        modifier = Modifier
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        InfoCard(
            drawableImage = R.drawable.place_icon,
            title = stringResource(id = R.string.visited_place_detail_screen_name),
            text = place.name ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.countries_icon,
            title = stringResource(id = R.string.visited_place_detail_screen_country),
            text = country?.name?.common ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.subregion_icon,
            title = stringResource(id = R.string.visited_place_detail_screen_region),
            text = place.region ?: ""
        )

        Spacer(modifier = Modifier.height(15.dp))

        Image(
            painter = painterResource(id = R.drawable.navigate_to_map_icon),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    if (place.latitude != null && place.longitude != null) {
                        try {
                            openMap(place.latitude!!.toDouble(), place.longitude!!.toDouble(), 14)
                        } catch (_: Exception) {}
                    }
                }
        )
    }
}