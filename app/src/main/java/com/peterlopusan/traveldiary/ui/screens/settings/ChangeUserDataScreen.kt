package com.peterlopusan.traveldiary.ui.screens.settings

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
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CountrySelector
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.utils.showToast

@Composable
fun ChangeUserDataScreen(navController: NavController) {
    val authViewModel = MainActivity.authViewModel
    val countryViewModel = MainActivity.countryViewModel
    val userInfo = authViewModel.userInfo
    var resetValues  by remember { mutableStateOf(true) }
    var firstname by remember { mutableStateOf(userInfo?.firstname ?: "") }
    var lastname by remember { mutableStateOf(userInfo?.lastname ?: "" ) }
    var selectedCountry by remember { mutableStateOf(countryViewModel.selectedCountry) }


    LaunchedEffect(key1 = true) {
        if (countryViewModel.selectedCountry == null) {
            val country = countryViewModel.getCountryFromShortname(authViewModel.userInfo?.countryCode)
            selectedCountry = country
            countryViewModel.selectedCountry = country
        }
    }

    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                countryViewModel.countryCodesForFiltering.clear()
                countryViewModel.selectedCountry = null
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
            title = stringResource(id = R.string.change_user_data_screen_toolbar_title),
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
                    painter = painterResource(id = R.drawable.travel_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.change_user_data_screen_firstname),
                    text = firstname,
                    onValueChange = {
                        firstname = it
                    },
                    icon = R.drawable.person_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.change_user_data_screen_lastname),
                    text = lastname,
                    onValueChange = {
                        lastname = it
                    },
                    icon = R.drawable.person_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                CountrySelector(
                    onValueChange = {},
                    hint = stringResource(id = R.string.change_user_data_screen_homeland_country),
                    text = selectedCountry?.name?.common ?: "",
                    flag = selectedCountry?.flags?.png,
                    modifier = Modifier
                        .clickable {
                            resetValues = false
                            countryViewModel.countryCodesForFiltering.clear()
                            selectedCountry?.shortname2?.let { countryViewModel.countryCodesForFiltering.add(it) }
                            navController.navigate(TravelDiaryRoutes.SelectCountryScreen.name)
                        }
                )

                Spacer(modifier = Modifier.height(25.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.change_user_data_screen_confirm_button),
                    clickAction = {
                        if (firstname.isBlank() || lastname.isBlank() || selectedCountry?.shortname2.isNullOrBlank()) {
                            showToast(MainActivity.instance.getString(R.string.create_account_missing_values))
                        } else {
                            authViewModel.saveUserData(firstname, lastname, selectedCountry?.shortname2!!, true)
                        }
                    }
                )
            }
        }
    }
}