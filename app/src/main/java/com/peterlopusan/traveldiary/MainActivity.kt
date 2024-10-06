package com.peterlopusan.traveldiary

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.screens.countries.AddOrEditCountryScreen
import com.peterlopusan.traveldiary.ui.screens.countries.SelectCountryScreen
import com.peterlopusan.traveldiary.ui.screens.countries.VisitedCountryDetailScreen
import com.peterlopusan.traveldiary.ui.screens.flights.AddOrEditFlightScreen
import com.peterlopusan.traveldiary.ui.screens.flights.CompletedFlightDetailScreen
import com.peterlopusan.traveldiary.ui.screens.flights.SelectAirportScreen
import com.peterlopusan.traveldiary.ui.screens.login.CreateAccount
import com.peterlopusan.traveldiary.ui.screens.login.ForgottenPasswordScreen
import com.peterlopusan.traveldiary.ui.screens.login.LoginScreen
import com.peterlopusan.traveldiary.ui.screens.main.MainScreen
import com.peterlopusan.traveldiary.ui.screens.places.AddOrEditPlaceScreen
import com.peterlopusan.traveldiary.ui.screens.places.GoogleMapsScreen
import com.peterlopusan.traveldiary.ui.screens.places.SelectCityScreen
import com.peterlopusan.traveldiary.ui.screens.places.VisitedPlaceDetailScreen
import com.peterlopusan.traveldiary.ui.screens.settings.AboutMyHomelandScreen
import com.peterlopusan.traveldiary.ui.screens.settings.ChangeUserDataScreen
import com.peterlopusan.traveldiary.ui.screens.settings.SettingsScreen
import com.peterlopusan.traveldiary.ui.theme.TravelDiaryTheme
import com.peterlopusan.traveldiary.viewModels.AuthViewModel
import com.peterlopusan.traveldiary.viewModels.CountryViewModel
import com.peterlopusan.traveldiary.viewModels.FlightViewModel
import com.peterlopusan.traveldiary.viewModels.MapViewModel
import com.peterlopusan.traveldiary.viewModels.PlaceViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        val language = SharedPreferencesManager().getLanguage()

        if (!language.isNullOrBlank()) {
            setLanguage(language)
        }

        setContent {
            TravelDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TravelDiaryAppScreen()
                }
            }
        }
    }

    private fun setLanguage(language: String) {
        val locale = Locale(language.lowercase())
        Locale.setDefault(locale)
        setResources(this.application.resources, locale)
        setResources(this.resources, locale)
    }

    private fun setResources(resources: Resources, locale: Locale) {
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }


    fun restartActivity() {
        recreate()
    }


    companion object {
        lateinit var instance: MainActivity
            private set

        lateinit var authViewModel: AuthViewModel
        lateinit var countryViewModel: CountryViewModel
        lateinit var flightViewModel: FlightViewModel
        lateinit var placeViewModel: PlaceViewModel
        lateinit var mapViewModel: MapViewModel
    }
}


@Composable
fun TravelDiaryAppScreen() {
    val navController = rememberNavController()

    MainActivity.authViewModel = viewModel()
    MainActivity.countryViewModel = viewModel()
    MainActivity.flightViewModel = viewModel()
    MainActivity.placeViewModel = viewModel()
    MainActivity.mapViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (MainActivity.authViewModel.isUserLogged()) TravelDiaryRoutes.MainScreen.name else TravelDiaryRoutes.Login.name
    ) {
        composable(route = TravelDiaryRoutes.Login.name) {
            LoginScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.MainScreen.name) {
            MainScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.CreateAccount.name) {
            CreateAccount(navController = navController)
        }

        composable(route = TravelDiaryRoutes.SelectCountryScreen.name) {
            SelectCountryScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.AddOrEditCountryScreen.name) {
            AddOrEditCountryScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.VisitedCountryDetailScreen.name) {
            VisitedCountryDetailScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.AddOrEditFlightScreen.name) {
            AddOrEditFlightScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.SelectAirportScreen.name) {
            SelectAirportScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.CompletedFlightDetailScreen.name) {
            CompletedFlightDetailScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.AddOrEditPlaceScreen.name) {
            AddOrEditPlaceScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.SelectCityScreen.name) {
            SelectCityScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.GoogleMapsScreen.name) {
            GoogleMapsScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.VisitedPlaceDetailScreen.name) {
            VisitedPlaceDetailScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.SettingsScreen.name) {
            SettingsScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.AboutMyHomelandScreen.name) {
            AboutMyHomelandScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.ChangeUserDataScreen.name) {
            ChangeUserDataScreen(navController = navController)
        }

        composable(route = TravelDiaryRoutes.ForgottenPasswordScreen.name) {
            ForgottenPasswordScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TravelDiaryAppPreview() {
    TravelDiaryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TravelDiaryAppScreen()
        }
    }
}