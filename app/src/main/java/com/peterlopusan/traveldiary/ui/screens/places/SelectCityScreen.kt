package com.peterlopusan.traveldiary.ui.screens.places


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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.data.models.place.City
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.openMap
import com.peterlopusan.traveldiary.utils.showLogs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCityScreen() {
    val viewModel = MainActivity.placeViewModel
    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    val citiesList: SnapshotStateList<City> = remember { mutableStateListOf() }
    var showBottomSheet by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState()
    var showLoading by remember { mutableStateOf(false) }

    var longitude by remember {
        if (viewModel.selectedLatLng == null) {
            mutableStateOf("")
        } else {
            mutableStateOf(String.format("%.4f", (viewModel.selectedLatLng?.longitude)))
        }
    }

    var latitude by remember {
        if (viewModel.selectedLatLng == null) {
            mutableStateOf("")
        } else {
            mutableStateOf(String.format("%.4f", (viewModel.selectedLatLng?.latitude)))
        }
    }

    LaunchedEffect(key1 = true) {
        if (longitude.isNotBlank() && latitude.isNotBlank()) {
            showLoading = true
            citiesList.clear()
            citiesList.addAll(viewModel.getCitiesList(searchText))
            showBottomSheet = false
            showLoading = false
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            title = stringResource(id = R.string.select_city_screen_toolbar_title),
            showBackButton = true
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.primaryBackground)
                .padding(20.dp)
        ) {
            LazyColumn {
                itemsIndexed(citiesList) { _, city ->
                    SelectCityItem(
                        modifier = Modifier
                            .clickable {
                                viewModel.visitedPlace?.visitedCity = city
                                MainActivity.navController.popBackStack()
                            },
                        city = city
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = MaterialTheme.colors.secondaryBackground,
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                        .background(MaterialTheme.colors.secondaryBackground)
                        .padding(15.dp)

                ) {

                    CustomTextField(
                        hint = stringResource(id = R.string.select_city_screen_search),
                        text = searchText,
                        borderColor = MaterialTheme.colors.secondaryTextColor,
                        onValueChange = { searchText = it },
                        startIcon = R.drawable.search_icon,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row {
                        CustomTextField(
                            hint = stringResource(id = R.string.select_city_screen_latitude),
                            text = latitude,
                            borderColor = MaterialTheme.colors.secondaryTextColor,
                            onValueChange = {

                            },
                            startIcon = R.drawable.navigate_to_map_icon,
                            modifier = Modifier.weight(1f),
                            clickAction = {
                                MainActivity.navController.navigate(TravelDiaryRoutes.GoogleMapsScreen.name)
                            }
                        )

                        Spacer(modifier = Modifier.width(15.dp))

                        CustomTextField(
                            inputType = KeyboardType.Number,
                            hint = stringResource(id = R.string.select_city_screen_longitude),
                            text = longitude,
                            borderColor = MaterialTheme.colors.secondaryTextColor,
                            onValueChange = {

                            },
                            startIcon = R.drawable.navigate_to_map_icon,
                            modifier = Modifier.weight(1f),
                            clickAction = {
                                MainActivity.navController.navigate(TravelDiaryRoutes.GoogleMapsScreen.name)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    CustomButton(
                        text = stringResource(id = R.string.select_city_screen_reset_button),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        searchText = ""
                        latitude = ""
                        longitude = ""
                        viewModel.selectedLatLng = null
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    CustomButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.select_city_screen_search_button),
                        clickAction = {
                            if (searchText.length > 2 || viewModel.selectedLatLng != null) {
                                showLoading = true
                                showBottomSheet = false
                                scope.launch {
                                    citiesList.clear()
                                    citiesList.addAll(viewModel.getCitiesList(searchText))
                                    showLoading = false
                                }
                            }
                        }
                    )
                }
            }
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                    .background(MaterialTheme.colors.secondaryBackground)
                    .padding(15.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_up_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            showBottomSheet = true
                        }
                )
            }
        }
    }

    LoadingIndicator(showLoading = showLoading)
}

@Composable
fun SelectCityItem(modifier: Modifier, city: City) {
    var countryName by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        val country = MainActivity.countryViewModel.getCountryFromShortname(city.country)
        countryName = country?.name?.common ?: ""
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondaryBackground)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(R.drawable.places_icon),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(
                text = city.name ?: "",
                color = MaterialTheme.colors.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                text = countryName,
                color = MaterialTheme.colors.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.navigate_to_map_icon),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Bottom)
                .clickable {
                    if (city.latitude != null && city.longitude != null) {
                        openMap(city.latitude!!.toDouble(), city.longitude!!.toDouble(), 14)
                    }
                }
        )
    }
}