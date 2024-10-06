package com.peterlopusan.traveldiary.ui.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.ConfirmAlertDialog
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import java.util.Locale

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel = MainActivity.authViewModel
    val sharedPref = SharedPreferencesManager()
    var showChangePasswordAlert by remember { mutableStateOf(false) }
    var showDeleteAccountAlert by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

    val darkDisplayMode by remember {
        when (MainActivity.instance.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                mutableStateOf(false)
            }

            else -> {
                mutableStateOf(true)
            }
        }
    }

    val language by remember {
        when (if (sharedPref.getLanguage().isNullOrBlank()) Locale.getDefault().country else sharedPref.getLanguage()) {
            "SK" -> {
                mutableStateOf(MainActivity.instance.getString(R.string.slovak))
            }

            else -> {
                mutableStateOf(MainActivity.instance.getString(R.string.english))
            }
        }
    }

    val displayModeName by remember { mutableStateOf(if (darkDisplayMode) MainActivity.instance.getString(R.string.settings_screen_dark_mode) else MainActivity.instance.getString(R.string.settings_screen_light_mode)) }


    if (showChangePasswordAlert) {
        ConfirmAlertDialog(
            confirmClick = {
                showChangePasswordAlert = !showChangePasswordAlert
                viewModel.resetPassword()
            },
            cancelClick = {
                showChangePasswordAlert = !showChangePasswordAlert
            },
            dialogTitle = stringResource(id = R.string.settings_screen_reset_password),
            dialogText = stringResource(id = R.string.settings_screen_are_you_sure_password)
        )
    }


    if (showDeleteAccountAlert) {
        ConfirmAlertDialog(
            confirmClick = {
                showLoading = true
                showDeleteAccountAlert = !showDeleteAccountAlert
                viewModel.deleteAccount {
                    navController.navigate(TravelDiaryRoutes.Login.name) {
                        popUpTo(TravelDiaryRoutes.MainScreen.name) { inclusive = true }
                    }
                }
            },
            cancelClick = {
                showDeleteAccountAlert = !showDeleteAccountAlert
            },
            dialogTitle = stringResource(id = R.string.settings_screen_delete_account),
            dialogText = stringResource(id = R.string.settings_screen_are_you_sure_account)
        )
    }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            navController = navController,
            title = stringResource(id = R.string.settings_screen_toolbar_title),
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
                    painter = painterResource(id = R.drawable.settings_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.settings_screen_language),
                    text = language,
                    onValueChange = {},
                    clickAction = {
                        if ((if (sharedPref.getLanguage().isNullOrBlank()) Locale.getDefault().country else sharedPref.getLanguage()) == "SK") {
                            sharedPref.setLanguage("EN")
                        } else {
                            sharedPref.setLanguage("SK")
                            MainActivity.instance.restartActivity()
                        }
                        MainActivity.instance.restartActivity()
                    },
                    icon = R.drawable.language_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomTextField(
                    hint = stringResource(id = R.string.settings_screen_display_mode),
                    text = displayModeName,
                    onValueChange = {},
                    clickAction = {
                        //todo
                    },
                    icon = if (darkDisplayMode) R.drawable.dark_mode_icon else R.drawable.light_mode_icon,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.settings_screen_about_my_homeland_button),
                    iconId = R.drawable.location_icon,
                    clickAction = {
                        navController.navigate(TravelDiaryRoutes.AboutMyHomelandScreen.name)
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.settings_screen_change_user_data_button),
                    iconId = R.drawable.person_icon,
                    clickAction = {
                        navController.navigate(TravelDiaryRoutes.ChangeUserDataScreen.name)
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.settings_screen_change_password_button),
                    iconId = R.drawable.password_icon,
                    clickAction = {
                        showChangePasswordAlert = true
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.settings_screen_delete_account_button),
                    iconId = R.drawable.delete_icon,
                    clickAction = {
                        showDeleteAccountAlert = true
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.settings_screen_logout_button),
                    iconId = R.drawable.logout_icon,
                    clickAction = {
                        SharedPreferencesManager().resetAllPreferences()
                        MainActivity.authViewModel.logout()
                        navController.navigate(TravelDiaryRoutes.Login.name) {
                            popUpTo(TravelDiaryRoutes.MainScreen.name) { inclusive = true }
                        }
                    }
                )
            }
        }
    }

    LoadingIndicator(showLoading = showLoading)
}