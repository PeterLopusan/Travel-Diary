package com.peterlopusan.traveldiary.ui.screens.countries

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.GsonBuilder
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.models.country.VisitedCountry
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CountrySelector
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.utils.getCalendarFromString
import com.peterlopusan.traveldiary.utils.showDatePicker
import com.peterlopusan.traveldiary.utils.showToast

@Composable
fun AddOrEditCountryScreen(navController: NavController) {
    val viewModel = MainActivity.countryViewModel
    val selectedCountry by remember { mutableStateOf(viewModel.selectedCountry) }
    var lastVisitDate by remember { mutableStateOf(viewModel.visitedCountry?.lastVisitDate ?: "") }
    var visitedPlaces by remember { mutableStateOf(viewModel.visitedCountry?.visitedPlaces ?: "") }
    var resetValues by remember { mutableStateOf(true) }
    var showLoading by remember { mutableStateOf(false) }
    val edit = viewModel.visitedCountry?.countryInfo != null


    LaunchedEffect(key1 = true) {
        if (viewModel.visitedCountry == null) {
            viewModel.visitedCountry = VisitedCountry()
        }
    }

    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                viewModel.selectedCountry = null
                viewModel.visitedCountry = null
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
            title = stringResource(id = if (edit) R.string.add_country_screen_toolbar_edit_title else R.string.add_country_screen_toolbar_add_title),
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
                    painter = painterResource(id = R.drawable.countries_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (!edit) {
                    CountrySelector(
                        onValueChange = {},
                        hint = stringResource(id = R.string.add_country_screen_country),
                        text = selectedCountry?.name?.common ?: "",
                        flag = selectedCountry?.flags?.png,
                        modifier = Modifier
                            .clickable {
                                resetValues = false
                                navController.navigate(TravelDiaryRoutes.SelectCountryScreen.name)
                            }
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }

                CustomTextField(
                    hint = stringResource(id = R.string.add_country_screen_last_visit_date),
                    text = lastVisitDate,
                    onValueChange = {},
                    clickAction = {
                        showDatePicker(getCalendarFromString(lastVisitDate)) { day, month, year ->
                            lastVisitDate = "${day}.${month}.${year}"
                            viewModel.visitedCountry?.lastVisitDate = lastVisitDate
                        }
                    },
                    icon = R.drawable.date_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.add_country_screen_visited_places),
                    text = visitedPlaces,
                    onValueChange = {
                        visitedPlaces = it
                        viewModel.visitedCountry?.visitedPlaces = it
                    },
                    icon = R.drawable.places_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(25.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = if (edit) R.string.add_country_screen_edit_button else R.string.add_country_screen_create_button),
                    clickAction = {
                        if (viewModel.selectedCountry != null && lastVisitDate.isNotBlank()) {
                            showLoading = true
                            Log.d("hackerman", GsonBuilder().create().toJson(selectedCountry))
                            viewModel.createOrEditCountryVisit().observe(MainActivity.instance) {
                                if (it == true) {
                                    if (edit) {
                                        resetValues = false
                                    }
                                    navController.popBackStack()
                                }
                                showLoading = false
                            }
                        } else {
                            showToast(MainActivity.instance.getString(R.string.add_country_screen_missing_values))
                        }
                    }
                )
            }
        }
    }

    LoadingIndicator(showLoading = showLoading)
}