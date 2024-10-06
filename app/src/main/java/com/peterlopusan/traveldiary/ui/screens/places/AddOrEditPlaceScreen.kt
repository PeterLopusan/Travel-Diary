package com.peterlopusan.traveldiary.ui.screens.places

import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.models.place.City
import com.peterlopusan.traveldiary.models.place.Place
import com.peterlopusan.traveldiary.models.place.VisitedPlace
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.utils.getCalendarFromString
import com.peterlopusan.traveldiary.utils.showDatePicker
import com.peterlopusan.traveldiary.utils.showToast

@Composable
fun AddOrEditPlaceScreen(navController: NavController) {
    val viewModel = MainActivity.placeViewModel
    var cityOrPlaceSelected by remember { mutableStateOf(viewModel.visitedPlace != null) }
    var mainImageUri by remember { mutableStateOf((viewModel.visitedPlace?.imageUrl)?.toUri()) }
    var resetValues by remember { mutableStateOf(true) }
    val edit = viewModel.visitedPlace?.id != null
    var lastVisitDate by remember { mutableStateOf(viewModel.visitedPlace?.lastVisitDate ?: "") }
    var note by remember { mutableStateOf(viewModel.visitedPlace?.note ?: "") }
    var showLoading by remember { mutableStateOf(false) }
    val launcherMain = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            mainImageUri = uri
            viewModel.visitedPlace?.imageUrl = it.toString()
        }
    }


    LaunchedEffect(key1 = true) {
        if (viewModel.visitedPlace == null) {
            viewModel.visitedPlace = VisitedPlace()
        }
    }

    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                viewModel.selectedLatLng = null
                viewModel.visitedPlace = null
            }
        }
    }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            navController = navController,
            title = stringResource(id = if (edit) R.string.add_place_screen_toolbar_edit_title else R.string.add_place_screen_toolbar_add_title),
            showBackButton = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalTravelDiaryColors.current.primaryBackground)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
                    .background(LocalTravelDiaryColors.current.secondaryBackground)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 15.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.places_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (cityOrPlaceSelected) {
                    if (viewModel.visitedPlace?.visitedCity != null) {
                        CityInfoCard(navController = navController) {
                            resetValues = false
                        }
                    } else {
                        PlaceInfoCard(navController = navController) {
                            resetValues = false
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    CustomTextField(
                        hint = stringResource(id = R.string.add_place_screen_note),
                        text = note,
                        onValueChange = {
                            note = it
                            viewModel.visitedPlace?.note = it
                        },
                        icon = R.drawable.note_icon,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    CustomTextField(
                        hint = stringResource(id = R.string.add_place_screen_last_visit_date),
                        text = lastVisitDate,
                        onValueChange = {},
                        clickAction = {
                            showDatePicker(getCalendarFromString(lastVisitDate)) { day, month, year ->
                                lastVisitDate = "${day}.${month}.${year}"
                                viewModel.visitedPlace?.lastVisitDate = lastVisitDate
                            }
                        },
                        icon = R.drawable.date_icon,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    CustomButton(
                        iconId = R.drawable.upload_icon,
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.add_place_screen_upload_image_button),
                        clickAction = {
                            launcherMain.launch("image/*")
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    mainImageUri?.let {
                        Box {
                            AsyncImage(
                                model = mainImageUri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(10))
                                    .border(2.dp, Color.Gray, RoundedCornerShape(10))
                                    .align(Alignment.Center)
                            )

                            Box(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.delete_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            mainImageUri = null
                                            viewModel.visitedPlace?.imageUrl = null
                                        }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                    }

                    CustomButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = if (edit) R.string.add_place_screen_button else R.string.add_place_screen_create_button),
                        clickAction = {
                            if (checkValues()) {
                                showLoading = true
                                viewModel.createOrEditPlaceVisit().observe(MainActivity.instance) {
                                    if (it == true) {
                                        if (edit) {
                                            resetValues = false
                                        }
                                        navController.popBackStack()
                                    }
                                    showLoading = false
                                }
                            }
                        }
                    )
                } else {
                    WhatDoYouWantToAdd { citySelected ->
                        if (citySelected) {
                            viewModel.visitedPlace?.visitedCity = City()
                        } else {
                            viewModel.visitedPlace?.visitedPlace = Place()
                        }
                        cityOrPlaceSelected = true
                    }
                }
            }
        }
    }

    LoadingIndicator(showLoading)
}

@Composable
fun WhatDoYouWantToAdd(selectAction: (Boolean) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.add_place_screen_do_you_want),
            color = LocalTravelDiaryColors.current.secondaryTextColor,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontFamily = fonts,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.add_place_screen_city),
            clickAction = {
                selectAction(true)
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = stringResource(id = R.string.add_place_screen_or),
            color = LocalTravelDiaryColors.current.secondaryTextColor,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontFamily = fonts,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(5.dp))

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.add_place_screen_place),
            clickAction = {
                selectAction(false)
            }
        )
    }
}

@Composable
fun CityInfoCard(navController: NavController, moveToCitySelectAction: () -> Unit) {
    val viewModel = MainActivity.placeViewModel
    val visitedCityName by remember { mutableStateOf(viewModel.visitedPlace?.visitedCity?.name ?: "") }
    var cityPopulation by remember {
        mutableStateOf(viewModel.visitedPlace?.visitedCity?.population ?: "")
    }

    var country by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        viewModel.visitedPlace?.visitedCity?.country?.let {
            country = MainActivity.countryViewModel.getCountryFromShortname(it)?.name?.common ?: ""
        }

        if (viewModel.selectedLatLng != null) {
            viewModel.visitedPlace?.visitedCity?.latitude = viewModel.selectedLatLng!!.latitude
            viewModel.visitedPlace?.visitedCity?.longitude = viewModel.selectedLatLng!!.longitude
        }

        viewModel.selectedLatLng = null
    }

    Column {
        CustomTextField(
            hint = stringResource(id = R.string.add_place_screen_visited_city),
            text = visitedCityName,
            onValueChange = {},
            clickAction = {
                moveToCitySelectAction()
                navController.navigate(TravelDiaryRoutes.SelectCityScreen.name)
            },
            icon = R.drawable.city_icon,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        CustomTextField(
            hint = stringResource(id = R.string.add_place_screen_visited_population),
            text = cityPopulation,
            onValueChange = {
                if (it.matches(Regex("\\d+")) && it.length < 9) {
                    viewModel.visitedPlace?.visitedCity?.population = it
                    cityPopulation = it
                } else if (it.isBlank()) {
                    viewModel.visitedPlace?.visitedCity?.population = null
                    cityPopulation = it
                }
            },
            icon = R.drawable.population_icon,
            modifier = Modifier.fillMaxWidth(),
            inputType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(15.dp))

        CustomTextField(
            hint = stringResource(id = R.string.add_place_screen_country),
            text = country,
            onValueChange = {},
            clickAction = {},
            icon = R.drawable.countries_icon,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PlaceInfoCard(navController: NavController, moveToMapAction: () -> Unit) {
    val viewModel = MainActivity.placeViewModel
    var visitedPlaceName by remember { mutableStateOf(viewModel.visitedPlace?.visitedPlace?.name ?: "") }
    var region by remember { mutableStateOf(viewModel.visitedPlace?.visitedPlace?.region ?: "") }
    var countryName by remember { mutableStateOf(viewModel.visitedPlace?.visitedPlace?.country ?: "") }
    var longitude by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        if (viewModel.selectedLatLng != null) {
            viewModel.visitedPlace?.visitedPlace?.latitude = viewModel.selectedLatLng!!.latitude
            viewModel.visitedPlace?.visitedPlace?.longitude = viewModel.selectedLatLng!!.longitude
            longitude = String.format("%.4f", (viewModel.visitedPlace?.visitedPlace?.longitude))
            latitude = String.format("%.4f", (viewModel.visitedPlace?.visitedPlace?.latitude))

            val address = getAddress(viewModel.selectedLatLng!!.latitude, viewModel.selectedLatLng!!.longitude)

            visitedPlaceName = address?.getAddressLine(0)?.split(',')?.get(0) ?: ""
            region = address?.adminArea ?: ""
            countryName = MainActivity.countryViewModel.getCountryFromShortname(address?.countryCode)?.name?.common ?: ""

            viewModel.visitedPlace?.visitedPlace?.name = visitedPlaceName
            viewModel.visitedPlace?.visitedPlace?.region = region
            viewModel.visitedPlace?.visitedPlace?.country = address?.countryCode
        }
    }

    Row {
        CustomTextField(
            hint = stringResource(id = R.string.add_place_screen_place_latitude),
            text = latitude,
            onValueChange = {},
            icon = R.drawable.navigate_to_map_icon,
            modifier = Modifier.weight(1f),
            clickAction = {
                moveToMapAction()
                navController.navigate(TravelDiaryRoutes.GoogleMapsScreen.name)
            }
        )

        Spacer(modifier = Modifier.width(15.dp))

        CustomTextField(
            hint = stringResource(id = R.string.add_place_screen_place_longitude),
            text = longitude,
            onValueChange = {},
            icon = R.drawable.navigate_to_map_icon,
            modifier = Modifier.weight(1f),
            clickAction = {
                moveToMapAction()
                navController.navigate(TravelDiaryRoutes.GoogleMapsScreen.name)
            }
        )
    }

    Spacer(modifier = Modifier.height(15.dp))

    CustomTextField(
        hint = stringResource(id = R.string.add_place_screen_place_name),
        text = visitedPlaceName,
        onValueChange = {
            visitedPlaceName = it
            viewModel.visitedPlace?.visitedPlace?.name = visitedPlaceName
        },
        icon = R.drawable.places_icon,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(15.dp))

    CustomTextField(
        hint = stringResource(id = R.string.add_place_screen_place_region),
        text = region,
        onValueChange = {
            region = it
            viewModel.visitedPlace?.visitedPlace?.region = region
        },
        icon = R.drawable.subregion_icon,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(15.dp))

    CustomTextField(
        hint = stringResource(id = R.string.add_place_screen_country),
        text = countryName,
        onValueChange = {
            countryName = it
            viewModel.visitedPlace?.visitedPlace?.country = it
        },
        icon = R.drawable.countries_icon,
        modifier = Modifier.fillMaxWidth()
    )
}

private fun checkValues(): Boolean {
    val viewModel = MainActivity.placeViewModel

    if (viewModel.visitedPlace?.visitedCity != null) {
        return if (viewModel.visitedPlace?.visitedCity?.name.isNullOrBlank() && viewModel.visitedPlace?.lastVisitDate.isNullOrBlank()) {
            showToast(MainActivity.instance.getString(R.string.add_place_screen_missing_values_city))
            false
        } else {
            true

        }
    } else if (viewModel.visitedPlace?.visitedPlace != null) {
        return if (viewModel.visitedPlace?.visitedPlace?.name.isNullOrBlank() && viewModel.visitedPlace?.lastVisitDate.isNullOrBlank() && viewModel.visitedPlace?.visitedPlace?.latitude == null) {
            showToast(MainActivity.instance.getString(R.string.add_place_screen_missing_values_place))
            false
        } else {
            true
        }
    }
    return false
}


private fun getAddress(latitude: Double, longitude: Double): Address? {
    val geocoder = Geocoder(MainActivity.instance)
    val addressList = geocoder.getFromLocation(latitude, longitude, 1)

    return if (addressList.isNullOrEmpty()) {
        null
    } else {
        return addressList[0]
    }
}


