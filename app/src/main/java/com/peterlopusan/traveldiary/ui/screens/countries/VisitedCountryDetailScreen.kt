package com.peterlopusan.traveldiary.ui.screens.countries

import androidx.compose.foundation.background
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.data.models.country.Country
import com.peterlopusan.traveldiary.data.models.country.CountryName
import com.peterlopusan.traveldiary.data.models.country.CurrenciesInfo
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.ConfirmAlertDialog
import com.peterlopusan.traveldiary.ui.components.InfoCard
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.utils.TranslateApiManager
import com.peterlopusan.traveldiary.utils.formatNumberWithSpaces
import com.peterlopusan.traveldiary.utils.getStringFromList
import com.peterlopusan.traveldiary.utils.removeSquareBrackets


@Composable
fun VisitedCountryDetailScreen() {
    val viewModel = MainActivity.countryViewModel
    var resetValues by remember { mutableStateOf(true) }
    val visitedCountry by remember { mutableStateOf(MainActivity.countryViewModel.visitedCountry) }
    var showAlert by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

    DisposableEffect(key1 = true) {
        this.onDispose {
            if (resetValues) {
                viewModel.selectedCountry = null
                viewModel.visitedCountry = null
            }
        }
    }

    if (showAlert) {
        ConfirmAlertDialog(
            confirmClick = {
                showLoading = true
                showAlert = !showAlert
                viewModel.deleteCountryVisit().observe(MainActivity.instance) {
                    if (it == true) {
                        MainActivity.navController.popBackStack()
                    }

                    showLoading = false
                }
            },
            cancelClick = {
                showAlert = !showAlert
            },
            dialogTitle = stringResource(id = R.string.visited_country_detail_screen_delete_title),
            dialogText = stringResource(id = R.string.visited_country_detail_screen_are_you_sure)
        )
    }


    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {

        Toolbar(
            title = stringResource(id = R.string.visited_country_detail_screen_toolbar_title),
            showBackButton = true,
            editButtonClick = {
                resetValues = false
                MainActivity.navController.navigate(TravelDiaryRoutes.AddOrEditCountryScreen.name)
            },
            deleteButtonClick = {
                showAlert = !showAlert
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryBackground)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.visited_country_detail_screen_about_your_visit),
                color = MaterialTheme.colors.primaryTextColor,
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
                    .background(MaterialTheme.colors.secondaryBackground)
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                InfoCard(
                    drawableImage = R.drawable.date_icon,
                    title = stringResource(id = R.string.visited_country_detail_screen_last_visit_date),
                    text = visitedCountry?.lastVisitDate ?: ""
                )

                Spacer(modifier = Modifier.height(20.dp))

                InfoCard(
                    drawableImage = R.drawable.places_icon,
                    title = stringResource(id = R.string.visited_country_detail_screen_visited_places),
                    text = visitedCountry?.visitedPlaces ?: ""
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(id = R.string.visited_country_detail_screen_about_country),
                color = MaterialTheme.colors.primaryTextColor,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            CountryInfoCard(countryInfo = visitedCountry?.countryInfo)

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    LoadingIndicator(showLoading = showLoading)
}


@Composable
fun CountryInfoCard(countryInfo: Country?) {
    var showOfficialName by remember { mutableStateOf(false) }
    val capitalList = remember { mutableStateListOf<String?>() }


    LaunchedEffect(key1 = true) {
        countryInfo?.capital?.forEach {
            capitalList.add(TranslateApiManager().translateCapital(it))
        }
    }

    Column(
        modifier = Modifier
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondaryBackground)
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        AsyncImage(
            model = countryInfo?.coatOfArms?.png,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))

        InfoCard(
            modifier = Modifier
                .clickable {
                    showOfficialName = !showOfficialName
                }
                .fillMaxWidth(),
            title = if (showOfficialName) countryInfo?.name?.official ?: "" else countryInfo?.name?.common ?: "",
            text = getStringFromList(prepareListOfName(countryInfo?.name?.nativeName?.getNativeCountryName(), showOfficialName)),
            urlImage = countryInfo?.flags?.png
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.places_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_capital_city),
            text = getStringFromList(capitalList),
            latlng = countryInfo?.capitalInfo?.latlng
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.language_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_language),
            text = getStringFromList(countryInfo?.languages?.getLanguage())
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.population_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_population),
            text = formatNumberWithSpaces(countryInfo?.population ?: 0)
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.continent_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_continent),
            text = removeSquareBrackets(countryInfo?.continents)
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.subregion_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_subregion),
            text = TranslateApiManager().translateSubregion(countryInfo?.subregion) ?: ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.sea_access_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_sea_access),
            text = stringResource(id = if (countryInfo?.landlocked == true) R.string.visited_country_detail_screen_no else R.string.visited_country_detail_screen_yes)
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.date_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_time_zones),
            text = getStringFromList(countryInfo?.timezones)
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.currency_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_currency),
            text = getStringFromList(prepareListOfCurrency(countryInfo?.currencies?.getCurrencyInfo()))
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            drawableImage = R.drawable.call_icon,
            title = stringResource(id = R.string.visited_country_detail_screen_call_number),
            text = getStringFromList(countryInfo?.callNumber?.getCallNumbers())
        )
    }
}

private fun prepareListOfCurrency(list: MutableList<CurrenciesInfo>?): MutableList<String> {
    val stringList = mutableListOf<String>()
    list?.forEach {
        stringList.add("${it.symbol ?: ""} - ${TranslateApiManager().translateCurrency(it.name) ?: ""}")
    }

    return stringList
}


private fun prepareListOfName(list: MutableList<CountryName>?, official: Boolean): MutableList<String> {
    val stringList = mutableListOf<String>()
    list?.forEach {
        stringList.add(if (official) it.official ?: "" else it.common ?: "")
    }

    return stringList
}


