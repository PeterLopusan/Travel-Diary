package com.peterlopusan.traveldiary.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.models.country.Country
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.screens.countries.CountryInfoCard
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors

@Composable
fun AboutMyHomelandScreen(navController: NavController) {
    val viewModel = MainActivity.countryViewModel
    var countryInfo by remember { mutableStateOf<Country?>(null) }

    LaunchedEffect(key1 = true) {
        countryInfo = viewModel.getCountryFromShortname(MainActivity.authViewModel.userInfo?.countryCode)
    }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            navController = navController,
            title = stringResource(id = R.string.about_my_homeland_screen_toolbar_title),
            showBackButton = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalTravelDiaryColors.current.primaryBackground)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            CountryInfoCard(countryInfo = countryInfo)

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}