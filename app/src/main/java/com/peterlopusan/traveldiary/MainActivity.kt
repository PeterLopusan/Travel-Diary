package com.peterlopusan.traveldiary

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
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
import com.peterlopusan.traveldiary.ui.viewModels.AuthViewModel
import com.peterlopusan.traveldiary.ui.viewModels.CountryViewModel
import com.peterlopusan.traveldiary.ui.viewModels.FlightViewModel
import com.peterlopusan.traveldiary.ui.viewModels.MapViewModel
import com.peterlopusan.traveldiary.ui.viewModels.PlaceViewModel
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
        val application = this.application
        val applicationResources = application.resources
        val activityResources = this.resources
        Locale.setDefault(locale)
        setResources(applicationResources, locale)
        setResources(activityResources, locale)
    }

    private fun setResources(resources: Resources, locale: Locale) {
        val displayMetrics = resources.displayMetrics
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, displayMetrics)
    }


    fun restartActivity() {
        recreate()
    }


    companion object {
        lateinit var instance: MainActivity
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController
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
    val authViewModel: AuthViewModel = viewModel()
    val countryViewModel: CountryViewModel = viewModel()
    val flightViewModel: FlightViewModel = viewModel()
    val placeViewModel: PlaceViewModel = viewModel()
    val mapViewModel: MapViewModel = viewModel()

    MainActivity.navController = navController
    MainActivity.authViewModel = authViewModel
    MainActivity.countryViewModel = countryViewModel
    MainActivity.flightViewModel = flightViewModel
    MainActivity.placeViewModel = placeViewModel
    MainActivity.mapViewModel = mapViewModel

    NavHost(
        navController = navController,
        startDestination = if (MainActivity.authViewModel.isUserLogged()) TravelDiaryRoutes.MainScreen.name else TravelDiaryRoutes.Login.name
    ) {
        composable(route = TravelDiaryRoutes.Login.name) {
            LoginScreen()
        }

        composable(route = TravelDiaryRoutes.MainScreen.name) {
            MainScreen()
        }

        composable(route = TravelDiaryRoutes.CreateAccount.name) {
            CreateAccount()
        }

        composable(route = TravelDiaryRoutes.SelectCountryScreen.name) {
            SelectCountryScreen()
        }

        composable(route = TravelDiaryRoutes.AddOrEditCountryScreen.name) {
            AddOrEditCountryScreen()
        }

        composable(route = TravelDiaryRoutes.VisitedCountryDetailScreen.name) {
            VisitedCountryDetailScreen()
        }

        composable(route = TravelDiaryRoutes.AddOrEditFlightScreen.name) {
            AddOrEditFlightScreen()
        }

        composable(route = TravelDiaryRoutes.SelectAirportScreen.name) {
            SelectAirportScreen()
        }

        composable(route = TravelDiaryRoutes.CompletedFlightDetailScreen.name) {
            CompletedFlightDetailScreen()
        }

        composable(route = TravelDiaryRoutes.AddOrEditPlaceScreen.name) {
            AddOrEditPlaceScreen()
        }

        composable(route = TravelDiaryRoutes.SelectCityScreen.name) {
            SelectCityScreen()
        }

        composable(route = TravelDiaryRoutes.GoogleMapsScreen.name) {
            GoogleMapsScreen()
        }

        composable(route = TravelDiaryRoutes.VisitedPlaceDetailScreen.name) {
            VisitedPlaceDetailScreen()
        }

        composable(route = TravelDiaryRoutes.SettingsScreen.name) {
            SettingsScreen()
        }

        composable(route = TravelDiaryRoutes.AboutMyHomelandScreen.name) {
            AboutMyHomelandScreen()
        }

        composable(route = TravelDiaryRoutes.ChangeUserDataScreen.name) {
            ChangeUserDataScreen()
        }

        composable(route = TravelDiaryRoutes.ForgottenPasswordScreen.name) {
            ForgottenPasswordScreen()
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