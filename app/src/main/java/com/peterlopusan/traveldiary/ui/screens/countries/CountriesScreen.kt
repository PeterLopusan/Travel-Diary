package com.peterlopusan.traveldiary.ui.screens.countries

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.enums.ContinentsEnum
import com.peterlopusan.traveldiary.enums.MainScreenEnums
import com.peterlopusan.traveldiary.models.country.VisitedCountry
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomCheckbox
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.SortAlertDialog
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.utils.removeSquareBrackets

@Composable
fun CountriesScreen(
    visitedCountryList: SnapshotStateList<VisitedCountry>,
    filteredCountryList: SnapshotStateList<VisitedCountry>,
    showSortDialog: MutableState<Boolean>,
    showFilterSheet: MutableState<Boolean>,
    navController: NavController
) {
    val viewModel = MainActivity.countryViewModel
    val sharedPref = SharedPreferencesManager()

    /**** FILTER ****/
    val searchText = remember { mutableStateOf(sharedPref.getCountrySearch()) }
    val europeChecked = remember { mutableStateOf(sharedPref.getIfContinentIsChecked(ContinentsEnum.EUROPE)) }
    val asiaChecked = remember { mutableStateOf(sharedPref.getIfContinentIsChecked(ContinentsEnum.ASIA)) }
    val africaChecked = remember { mutableStateOf(sharedPref.getIfContinentIsChecked(ContinentsEnum.AFRICA)) }
    val oceaniaChecked = remember { mutableStateOf(sharedPref.getIfContinentIsChecked(ContinentsEnum.OCEANIA)) }
    val antarcticaChecked = remember { mutableStateOf(sharedPref.getIfContinentIsChecked(ContinentsEnum.ANTARCTICA)) }
    val northAmericaChecked = remember { mutableStateOf(sharedPref.getIfContinentIsChecked(ContinentsEnum.NORTH_AMERICA)) }
    val southAmericaChecked = remember { mutableStateOf(sharedPref.getIfContinentIsChecked(ContinentsEnum.SOUTH_AMERICA)) }


    LaunchedEffect(key1 = true) {
        viewModel.countryCodesForFiltering.clear()
        visitedCountryList.forEach {
            viewModel.countryCodesForFiltering.add(it.countryCode)
        }
        MainActivity.authViewModel.userInfo?.countryCode?.let { viewModel.countryCodesForFiltering.add(it) }
    }

    if (showSortDialog.value) {
        SortAlertDialog(
            showFlightDuration = false,
            screenType = MainScreenEnums.COUNTRY,
            showName = true,
            closeClick = { showSortDialog.value = false },
            sortChanged = {
                viewModel.sortCountryList(filteredCountryList)
            }
        )
    }

    if (showFilterSheet.value) {
        SelectCountryFilterSheet(
            showFilterSheet = showFilterSheet,
            filteredCountryList = filteredCountryList,
            countryList = visitedCountryList,
            searchText = searchText,
            europeChecked = europeChecked,
            asiaChecked = asiaChecked,
            africaChecked = africaChecked,
            oceaniaChecked = oceaniaChecked,
            antarcticaChecked = antarcticaChecked,
            northAmericaChecked = northAmericaChecked,
            southAmericaChecked = southAmericaChecked
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .padding(horizontal = 20.dp)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = if (visitedCountryList.size == 1) R.string.countries_screen_visited_countries_singular else R.string.countries_screen_visited_countries_plural,
                visitedCountryList.size
            ),
            color = LocalTravelDiaryColors.current.primaryTextColor,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fonts,
                textDecoration = TextDecoration.Underline
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn {
            itemsIndexed(filteredCountryList) { _, visitedCountry ->
                VisitedCountryItem(
                    modifier = Modifier
                        .clickable {
                            viewModel.selectedCountry = visitedCountry.countryInfo
                            viewModel.visitedCountry = visitedCountry
                            navController.navigate(TravelDiaryRoutes.VisitedCountryDetailScreen.name)
                        },
                    visitedCountry = visitedCountry
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun VisitedCountryItem(
    modifier: Modifier = Modifier,
    visitedCountry: VisitedCountry
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(LocalTravelDiaryColors.current.secondaryBackground)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = visitedCountry.countryInfo?.flags?.png,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(
                text = visitedCountry.countryInfo?.name?.common ?: "",
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                text = stringResource(id = R.string.countries_screen_continent, removeSquareBrackets(visitedCountry.countryInfo?.continents)),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.countries_screen_last_visit_date, visitedCountry.lastVisitDate),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.countries_screen_visited_places, visitedCountry.visitedPlaces),
                color = LocalTravelDiaryColors.current.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectCountryFilterSheet(
    showFilterSheet: MutableState<Boolean>,
    filteredCountryList: SnapshotStateList<VisitedCountry>,
    countryList: SnapshotStateList<VisitedCountry>,
    searchText: MutableState<String>,
    europeChecked: MutableState<Boolean>,
    asiaChecked: MutableState<Boolean>,
    africaChecked: MutableState<Boolean>,
    oceaniaChecked: MutableState<Boolean>,
    antarcticaChecked: MutableState<Boolean>,
    northAmericaChecked: MutableState<Boolean>,
    southAmericaChecked: MutableState<Boolean>,
) {
    val sheetState = rememberModalBottomSheetState()
    val sharedPref = SharedPreferencesManager()
    val viewModel = MainActivity.countryViewModel

    ModalBottomSheet(
        containerColor = LocalTravelDiaryColors.current.secondaryBackground,
        onDismissRequest = { showFilterSheet.value = false },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(bottom = 30.dp)
        ) {
            CustomTextField(
                hint = stringResource(id = R.string.search),
                text = searchText.value,
                borderColor = LocalTravelDiaryColors.current.secondaryTextColor,
                onValueChange = {
                    searchText.value = it
                    sharedPref.setCountrySearch(it)
                    viewModel.filterCountryList(
                        filteredCountryList = filteredCountryList,
                        countryList = countryList
                    )
                },
                icon = R.drawable.search_icon,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(LocalTravelDiaryColors.current.primaryTextColor)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.countries_screen_filter_continent),
                color = LocalTravelDiaryColors.current.primaryTextColor,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Column {
                    CustomCheckbox(
                        text = stringResource(id = R.string.europe),
                        isChecked = europeChecked,
                        onCheckedChange = { isChecked ->
                            europeChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.EUROPE)
                            viewModel.filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.north_america),
                        isChecked = northAmericaChecked,
                        onCheckedChange = { isChecked ->
                            northAmericaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.NORTH_AMERICA)
                            viewModel.filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.south_america),
                        isChecked = southAmericaChecked,
                        onCheckedChange = { isChecked ->
                            southAmericaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.SOUTH_AMERICA)
                            viewModel.filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.width(30.dp))

                Column {

                    CustomCheckbox(
                        text = stringResource(id = R.string.asia),
                        isChecked = asiaChecked,
                        onCheckedChange = { isChecked ->
                            asiaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.ASIA)
                            viewModel.filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.africa),
                        isChecked = africaChecked,
                        onCheckedChange = { isChecked ->
                            africaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.AFRICA)
                            viewModel.filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.oceania),
                        isChecked = oceaniaChecked,
                        onCheckedChange = { isChecked ->
                            oceaniaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.OCEANIA)
                            viewModel.filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.flights_screen_filter_reset_button),
                clickAction = {
                    searchText.value = ""
                    europeChecked.value = true
                    asiaChecked.value = true
                    africaChecked.value = true
                    oceaniaChecked.value = true
                    antarcticaChecked.value = true
                    europeChecked.value = true
                    southAmericaChecked.value = true
                    northAmericaChecked.value = true
                    sharedPref.resetCountryFilter()
                    filteredCountryList.clear()
                    filteredCountryList.addAll(countryList)
                    viewModel.sortCountryList(filteredCountryList)
                }
            )
        }
    }
}
