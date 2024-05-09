package com.peterlopusan.traveldiary.ui.screens.login

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CountrySelector
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.TravelDiaryTheme
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.utils.showLogs
import com.peterlopusan.traveldiary.utils.showToast

@Composable
fun CreateAccount() {
    val countryViewModel = MainActivity.countryViewModel
    val authViewModel = MainActivity.authViewModel
    var firstname by remember { mutableStateOf(authViewModel.createBody.firstname) }
    var lastname by remember { mutableStateOf(authViewModel.createBody.lastname) }
    var email by remember { mutableStateOf(authViewModel.createBody.email) }
    var password by remember { mutableStateOf(authViewModel.createBody.password) }
    var confirmPassword by remember { mutableStateOf(authViewModel.createBody.confirmPassword) }
    val selectedCountry by remember { mutableStateOf(countryViewModel.selectedCountry) }
    var resetValues  by remember { mutableStateOf(true) }
    var showLoading by remember { mutableStateOf(false) }


    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                countryViewModel.selectedCountry = null
                authViewModel.resetCreateBody()
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            title = stringResource(id = R.string.create_account_screen_toolbar_title),
            showBackButton = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryBackground)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.secondaryBackground)
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
                    hint = stringResource(id = R.string.create_account_screen_firstname),
                    text = firstname,
                    onValueChange = {
                        firstname = it
                        authViewModel.createBody.firstname = it
                    },
                    startIcon = R.drawable.person_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.create_account_screen_lastname),
                    text = lastname,
                    onValueChange = {
                        lastname = it
                        authViewModel.createBody.lastname = it
                    },
                    startIcon = R.drawable.person_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.create_account_screen_email),
                    text = email,
                    onValueChange = {
                        email = it
                        authViewModel.createBody.email = it
                    },
                    startIcon = R.drawable.email_icon,
                    modifier = Modifier.fillMaxWidth(),
                    inputType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.create_account_screen_password),
                    text = password,
                    onValueChange = {
                        password = it
                        authViewModel.createBody.password = it
                    },
                    startIcon = R.drawable.password_icon,
                    passwordInput = true,
                    modifier = Modifier.fillMaxWidth(),
                    inputType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.create_account_screen_confirm_password),
                    text = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        authViewModel.createBody.confirmPassword = it
                    },
                    startIcon = R.drawable.password_icon,
                    passwordInput = true,
                    modifier = Modifier.fillMaxWidth(),
                    inputType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(15.dp))

                CountrySelector(
                    onValueChange = {},
                    hint = stringResource(id = R.string.create_account_screen_homeland_country),
                    text = selectedCountry?.name?.common ?: "",
                    flag = selectedCountry?.flags?.png,
                    modifier = Modifier
                        .clickable {
                            resetValues = false
                            countryViewModel.countryCodesForFiltering.clear()
                            MainActivity.navController.navigate(TravelDiaryRoutes.SelectCountryScreen.name)
                        }
                )

                Spacer(modifier = Modifier.height(25.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.create_account_screen_create_button),
                    clickAction = {
                        if (firstname.isBlank() || lastname.isBlank() || email.isBlank() || password.isBlank() || selectedCountry?.shortname2.isNullOrBlank()) {
                            showToast(MainActivity.instance.getString(R.string.create_account_missing_values))
                        } else if (password != confirmPassword) {
                            showToast(MainActivity.instance.getString(R.string.create_account_password_not_match))
                        } else {
                            showLoading = true
                            authViewModel.createAccount(countryCode = selectedCountry?.shortname2 ?: "").observe(MainActivity.instance) { isSuccessful ->
                                if (isSuccessful) {
                                    MainActivity.navController.navigate(TravelDiaryRoutes.MainScreen.name) {
                                        popUpTo(TravelDiaryRoutes.Login.name) { inclusive = true }
                                    }
                                }

                                showLoading = false
                            }
                        }
                    }
                )
            }
        }
    }

    LoadingIndicator(showLoading = showLoading)
}

@Preview(showBackground = true)
@Composable
fun CreateAccountPreview() {
    TravelDiaryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CreateAccount()
        }
    }
}