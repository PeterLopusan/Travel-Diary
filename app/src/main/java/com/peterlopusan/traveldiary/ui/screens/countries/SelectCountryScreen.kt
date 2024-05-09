package com.peterlopusan.traveldiary.ui.screens.countries

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.painterResource
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
import com.peterlopusan.traveldiary.data.models.country.Country
import com.peterlopusan.traveldiary.europe
import com.peterlopusan.traveldiary.northAmerica
import com.peterlopusan.traveldiary.oceania
import com.peterlopusan.traveldiary.southAmerica
import com.peterlopusan.traveldiary.ui.components.CustomCheckbox
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.TranslateApiManager
import com.peterlopusan.traveldiary.utils.openMap
import com.peterlopusan.traveldiary.utils.removeSquareBrackets


@Composable
fun SelectCountryScreen() {
    val viewModel = MainActivity.countryViewModel
    val countryList: SnapshotStateList<Country> = remember { mutableStateListOf() }
    val filteredCountryList: SnapshotStateList<Country> = remember { mutableStateListOf() }
    var showLoading by remember { mutableStateOf(true) }
    val showBottomSheet = remember { mutableStateOf(false) }


    /**** FILTER ****/
    val searchText = remember { mutableStateOf("") }
    val europeChecked = remember { mutableStateOf(true) }
    val asiaChecked = remember { mutableStateOf(true) }
    val africaChecked = remember { mutableStateOf(true) }
    val oceaniaChecked = remember { mutableStateOf(true) }
    val antarcticaChecked = remember { mutableStateOf(true) }
    val northAmericaChecked = remember { mutableStateOf(true) }
    val southAmericaChecked = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        val list = viewModel.getCountriesList()
        countryList.addAll(list)
        filteredCountryList.addAll(list)
        showLoading = false
    }

    DisposableEffect(key1 = true) {
        this.onDispose {
            viewModel.countryCodesForFiltering.clear()
        }
    }

    if (showBottomSheet.value) {
        SelectCountryFilterSheet(
            showFilterSheet = showBottomSheet,
            filteredCountryList = filteredCountryList,
            countryList = countryList,
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
            .background(MaterialTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            title = stringResource(id = R.string.select_country_screen_toolbar_title),
            showBackButton = true,
            filterButtonClick = { showBottomSheet.value = true }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryBackground)
                .padding(20.dp)
        ) {
            LazyColumn {
                itemsIndexed(filteredCountryList) { _, country ->
                    SelectCountryItem(
                        modifier = Modifier
                            .clickable {
                                viewModel.selectedCountry = country
                                MainActivity.navController.popBackStack()
                            },
                        country = country
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    LoadingIndicator(showLoading = showLoading)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectCountryFilterSheet(
    showFilterSheet: MutableState<Boolean>,
    filteredCountryList: SnapshotStateList<Country>,
    countryList: SnapshotStateList<Country>,
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
    val allChecked = remember { mutableStateOf(europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value) }


    ModalBottomSheet(
        containerColor = MaterialTheme.colors.secondaryBackground,
        onDismissRequest = {
            showFilterSheet.value = false
        },
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
                text = stringResource(id = R.string.select_country_filter_continent),
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
                        text = stringResource(id = R.string.select_country_filter_all),
                        isChecked = allChecked,
                        onCheckedChange = {
                            europeChecked.value = it
                            asiaChecked.value = it
                            africaChecked.value = it
                            oceaniaChecked.value = it
                            antarcticaChecked.value = it
                            northAmericaChecked.value = it
                            southAmericaChecked.value = it
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
                        text = stringResource(id = R.string.europe),
                        isChecked = europeChecked,
                        onCheckedChange = { isChecked ->
                            europeChecked.value = isChecked
                            allChecked.value =
                                europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value
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
                            allChecked.value =
                                europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value
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
                            allChecked.value =
                                europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value
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
                            allChecked.value =
                                europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value
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
                            allChecked.value =
                                europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value
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
                            allChecked.value =
                                europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value
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
                            allChecked.value =
                                europeChecked.value && asiaChecked.value && africaChecked.value && oceaniaChecked.value && antarcticaChecked.value && southAmericaChecked.value && northAmericaChecked.value
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
        }
    }
}


@Composable
fun SelectCountryItem(modifier: Modifier, country: Country) {
    val capitalList = remember { mutableStateListOf<String?>() }

    LaunchedEffect(key1 = true) {
        country.capital?.forEach {
            capitalList.add(TranslateApiManager().translateCapital(it))
        }
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondaryBackground)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = country.flags?.png,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(60.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = country.name?.common ?: "",
                color = MaterialTheme.colors.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                    textDecoration = TextDecoration.Underline
                )
            )

            Text(
                text = stringResource(id = R.string.select_country_continent, removeSquareBrackets(country.continents)),
                color = MaterialTheme.colors.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )

            Text(
                text = stringResource(id = R.string.select_country_subregion, TranslateApiManager().translateSubregion(country.subregion) ?: ""),
                color = MaterialTheme.colors.secondaryTextColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts
                )
            )
        }

        Image(
            painter = painterResource(R.drawable.navigate_to_map_icon),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Top)
                .clickable {
                    country.latlng?.let {
                        if (country.latlng.size >= 2) {
                            val latitude = country.latlng[0]
                            val longitude = country.latlng[1]
                            openMap(latitude, longitude, 6)
                        }
                    }
                }
        )
    }
}

private fun filterCountryList(
    filteredCountryList: SnapshotStateList<Country>,
    countryList: SnapshotStateList<Country>,
    searchText: String,
    europeChecked: Boolean,
    asiaChecked: Boolean,
    africaChecked: Boolean,
    oceaniaChecked: Boolean,
    antarcticaChecked: Boolean,
    northAmericaChecked: Boolean,
    southAmericaChecked: Boolean
) {
    var list = mutableListOf<Country>()
    list.addAll(countryList)

    if (searchText.isNotBlank()) {
        list = list.filter { it.name?.common?.contains(searchText) == true || it.name?.official?.contains(searchText) == true }.toMutableList()
    }

    if (!europeChecked) {
        list = list.filterNot { it.continents?.contains(europe) == true }.toMutableList()
    }

    if (!asiaChecked) {
        list = list.filterNot { it.continents?.contains(asia) == true }.toMutableList()
    }

    if (!africaChecked) {
        list = list.filterNot { it.continents?.contains(africa) == true }.toMutableList()
    }

    if (!oceaniaChecked) {
        list = list.filterNot { it.continents?.contains(oceania) == true }.toMutableList()
    }

    if (!antarcticaChecked) {
        list = list.filterNot { it.continents?.contains(antarctica) == true }.toMutableList()
    }

    if (!northAmericaChecked) {
        list = list.filterNot { it.continents?.contains(northAmerica) == true }.toMutableList()
    }

    if (!southAmericaChecked) {
        list = list.filterNot { it.continents?.contains(southAmerica) == true }.toMutableList()
    }

    filteredCountryList.clear()
    filteredCountryList.addAll(list)
}