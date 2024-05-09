package com.peterlopusan.traveldiary.ui.screens.countries

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.africa
import com.peterlopusan.traveldiary.antarctica
import com.peterlopusan.traveldiary.asia
import com.peterlopusan.traveldiary.data.enums.ContinentsEnum
import com.peterlopusan.traveldiary.data.enums.MainScreenEnums
import com.peterlopusan.traveldiary.data.enums.SortTypeEnum
import com.peterlopusan.traveldiary.data.models.country.VisitedCountry
import com.peterlopusan.traveldiary.europe
import com.peterlopusan.traveldiary.northAmerica
import com.peterlopusan.traveldiary.oceania
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.southAmerica
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomCheckbox
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.SortAlertDialog
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.editStrings
import com.peterlopusan.traveldiary.utils.formatDate
import com.peterlopusan.traveldiary.utils.removeSquareBrackets

@Composable
fun CountriesScreen(showSortDialog: MutableState<Boolean>, showFilterSheet: MutableState<Boolean>) {
    val visitedCountryList: SnapshotStateList<VisitedCountry> = remember { mutableStateListOf() }
    val filteredCountryList: SnapshotStateList<VisitedCountry> = remember { mutableStateListOf() }
    var showLoading by remember { mutableStateOf(true) }
    val showBottomSheet = remember { mutableStateOf(false) }
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
        visitedCountryList.addAll(viewModel.getVisitedCountriesList())
        filteredCountryList.addAll(visitedCountryList)
        filterCountryList(
            filteredCountryList = filteredCountryList,
            countryList = visitedCountryList,
            searchText = searchText.value,
            europeChecked = europeChecked.value,
            asiaChecked = asiaChecked.value,
            africaChecked = africaChecked.value,
            oceaniaChecked = oceaniaChecked.value,
            antarcticaChecked = antarcticaChecked.value,
            southAmericaChecked = southAmericaChecked.value,
            northAmericaChecked = northAmericaChecked.value
        )
        showLoading = false
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
                sortCountryList(visitedCountryList)
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

    Box {
        if (showLoading) {
            LoadingIndicator(showLoading = true)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primaryBackground)
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = if (visitedCountryList.size == 1) R.string.countries_screen_visited_countries_singular else R.string.countries_screen_visited_countries_plural, visitedCountryList.size),
                    color = MaterialTheme.colors.primaryTextColor,
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
                                    MainActivity.navController.navigate(TravelDiaryRoutes.VisitedCountryDetailScreen.name)
                                },
                            visitedCountry = visitedCountry
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
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
            .background(MaterialTheme.colors.secondaryBackground)
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
                color = MaterialTheme.colors.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                text = stringResource(id = R.string.countries_screen_continent, removeSquareBrackets(visitedCountry.countryInfo?.continents)),
                color = MaterialTheme.colors.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.countries_screen_last_visit_date, visitedCountry.lastVisitDate),
                color = MaterialTheme.colors.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.countries_screen_visited_places, visitedCountry.visitedPlaces),
                color = MaterialTheme.colors.secondaryTextColor,
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

    ModalBottomSheet(
        containerColor = MaterialTheme.colors.secondaryBackground,
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
                borderColor = MaterialTheme.colors.secondaryTextColor,
                onValueChange = {
                    searchText.value = it
                    sharedPref.setCountrySearch(it)
                    filterCountryList(
                        filteredCountryList = filteredCountryList,
                        countryList = countryList,
                        searchText = searchText.value,
                        europeChecked = europeChecked.value,
                        asiaChecked = asiaChecked.value,
                        africaChecked = africaChecked.value,
                        oceaniaChecked = oceaniaChecked.value,
                        antarcticaChecked = antarcticaChecked.value,
                        southAmericaChecked = southAmericaChecked.value,
                        northAmericaChecked = northAmericaChecked.value
                    )
                },
                startIcon = R.drawable.search_icon,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(MaterialTheme.colors.primaryTextColor)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = R.string.countries_screen_filter_continent),
                color = MaterialTheme.colors.primaryTextColor,
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
                            filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList,
                                searchText = searchText.value,
                                europeChecked = europeChecked.value,
                                asiaChecked = asiaChecked.value,
                                africaChecked = africaChecked.value,
                                oceaniaChecked = oceaniaChecked.value,
                                antarcticaChecked = antarcticaChecked.value,
                                southAmericaChecked = southAmericaChecked.value,
                                northAmericaChecked = northAmericaChecked.value
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.north_america),
                        isChecked = northAmericaChecked,
                        onCheckedChange = { isChecked ->
                            northAmericaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.NORTH_AMERICA)
                            filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList,
                                searchText = searchText.value,
                                europeChecked = europeChecked.value,
                                asiaChecked = asiaChecked.value,
                                africaChecked = africaChecked.value,
                                oceaniaChecked = oceaniaChecked.value,
                                antarcticaChecked = antarcticaChecked.value,
                                southAmericaChecked = southAmericaChecked.value,
                                northAmericaChecked = northAmericaChecked.value
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.south_america),
                        isChecked = southAmericaChecked,
                        onCheckedChange = { isChecked ->
                            southAmericaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.SOUTH_AMERICA)
                            filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList,
                                searchText = searchText.value,
                                europeChecked = europeChecked.value,
                                asiaChecked = asiaChecked.value,
                                africaChecked = africaChecked.value,
                                oceaniaChecked = oceaniaChecked.value,
                                antarcticaChecked = antarcticaChecked.value,
                                southAmericaChecked = southAmericaChecked.value,
                                northAmericaChecked = northAmericaChecked.value
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
                            filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList,
                                searchText = searchText.value,
                                europeChecked = europeChecked.value,
                                asiaChecked = asiaChecked.value,
                                africaChecked = africaChecked.value,
                                oceaniaChecked = oceaniaChecked.value,
                                antarcticaChecked = antarcticaChecked.value,
                                southAmericaChecked = southAmericaChecked.value,
                                northAmericaChecked = northAmericaChecked.value
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.africa),
                        isChecked = africaChecked,
                        onCheckedChange = { isChecked ->
                            africaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.AFRICA)
                            filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList,
                                searchText = searchText.value,
                                europeChecked = europeChecked.value,
                                asiaChecked = asiaChecked.value,
                                africaChecked = africaChecked.value,
                                oceaniaChecked = oceaniaChecked.value,
                                antarcticaChecked = antarcticaChecked.value,
                                southAmericaChecked = southAmericaChecked.value,
                                northAmericaChecked = northAmericaChecked.value
                            )
                        }
                    )

                    CustomCheckbox(
                        text = stringResource(id = R.string.oceania),
                        isChecked = oceaniaChecked,
                        onCheckedChange = { isChecked ->
                            oceaniaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.OCEANIA)
                            filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList,
                                searchText = searchText.value,
                                europeChecked = europeChecked.value,
                                asiaChecked = asiaChecked.value,
                                africaChecked = africaChecked.value,
                                oceaniaChecked = oceaniaChecked.value,
                                antarcticaChecked = antarcticaChecked.value,
                                southAmericaChecked = southAmericaChecked.value,
                                northAmericaChecked = northAmericaChecked.value
                            )
                        }
                    )

                    /** for independent == false ↓ **/
                    /*
                    CustomCheckbox(
                        text = stringResource(id = R.string.antarctica),
                        isChecked = antarcticaChecked,
                        onCheckedChange = { isChecked ->
                            antarcticaChecked.value = isChecked
                            sharedPref.setIfContinentIsChecked(isChecked, ContinentsEnum.ANTARCTICA)
                            filterCountryList(
                                filteredCountryList = filteredCountryList,
                                countryList = countryList,
                                searchText = searchText.value,
                                europeChecked = europeChecked.value,
                                asiaChecked = asiaChecked.value,
                                africaChecked = africaChecked.value,
                                oceaniaChecked = oceaniaChecked.value,
                                antarcticaChecked = antarcticaChecked.value,
                                southAmericaChecked = southAmericaChecked.value,
                                northAmericaChecked = northAmericaChecked.value
                            )
                        }
                    )
                    */
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
                    sortCountryList(filteredCountryList)
                }
            )
        }
    }
}

private fun filterCountryList(
    filteredCountryList: SnapshotStateList<VisitedCountry>,
    countryList: SnapshotStateList<VisitedCountry>,
    searchText: String,
    europeChecked: Boolean,
    asiaChecked: Boolean,
    africaChecked: Boolean,
    oceaniaChecked: Boolean,
    antarcticaChecked: Boolean,
    northAmericaChecked: Boolean,
    southAmericaChecked: Boolean
) {
    var list = mutableListOf<VisitedCountry>()
    list.addAll(countryList)

    if (searchText.isNotBlank()) {
        list = list.filter { it.countryInfo?.name?.common?.contains(searchText) == true || it.countryInfo?.name?.official?.contains(searchText) == true }.toMutableList()
    }

    if (!europeChecked) {
        list = list.filterNot { it.countryInfo?.continents?.contains(europe) == true }.toMutableList()
    }

    if (!asiaChecked) {
        list = list.filterNot { it.countryInfo?.continents?.contains(asia) == true }.toMutableList()
    }

    if (!africaChecked) {
        list = list.filterNot { it.countryInfo?.continents?.contains(africa) == true }.toMutableList()
    }

    if (!oceaniaChecked) {
        list = list.filterNot { it.countryInfo?.continents?.contains(oceania) == true }.toMutableList()
    }

    if (!antarcticaChecked) {
        list = list.filterNot { it.countryInfo?.continents?.contains(antarctica) == true }.toMutableList()
    }

    if (!northAmericaChecked) {
        list = list.filterNot { it.countryInfo?.continents?.contains(northAmerica) == true }.toMutableList()
    }

    if (!southAmericaChecked) {
        list = list.filterNot { it.countryInfo?.continents?.contains(southAmerica) == true }.toMutableList()
    }

    filteredCountryList.clear()
    filteredCountryList.addAll(list)
    sortCountryList(filteredCountryList)
}

private fun sortCountryList(list: SnapshotStateList<VisitedCountry>) {
    when (SharedPreferencesManager().getCountrySortPreference()) {
        SortTypeEnum.DATE_OLDEST_FIRST -> {
            list.sortBy { formatDate(it.lastVisitDate, "dd.MM.yyyy") }
        }

        SortTypeEnum.DATE_NEWEST_FIRST -> {
            list.sortBy { formatDate(it.lastVisitDate, "dd.MM.yyyy") }
            list.reverse()
        }

        SortTypeEnum.NAME_ABC -> {
            list.sortBy { editStrings(it.countryInfo?.name?.common) }
        }

        SortTypeEnum.NAME_ZYX -> {
            list.sortBy { editStrings(it.countryInfo?.name?.common) }
            list.reverse()
        }

        else -> {}
    }
}
