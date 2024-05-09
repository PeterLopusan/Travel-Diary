package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.data.enums.MainScreenEnums
import com.peterlopusan.traveldiary.data.enums.SortTypeEnum
import com.peterlopusan.traveldiary.sharedPreferences.SharedPreferencesManager
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground

@Composable
fun SortAlertDialog(
    modifier: Modifier = Modifier,
    screenType: MainScreenEnums,
    showFlightDuration: Boolean,
    showName: Boolean,
    closeClick: () -> Unit,
    sortChanged: () -> Unit
) {
    val selectedSort = when(screenType) {
        MainScreenEnums.FLIGHT -> SharedPreferencesManager().getFlightSortPreference()
        MainScreenEnums.PLACE -> SharedPreferencesManager().getPlaceSortPreference()
        MainScreenEnums.COUNTRY -> SharedPreferencesManager().getCountrySortPreference()
    }

    val dateNewestSelected = remember { mutableStateOf(selectedSort == SortTypeEnum.DATE_NEWEST_FIRST) }
    val dateOldestSelected = remember { mutableStateOf(selectedSort == SortTypeEnum.DATE_OLDEST_FIRST) }

    val durationLongestSelected = remember { mutableStateOf(selectedSort == SortTypeEnum.DURATION_LONGEST_FIRST) }
    val durationShortestSelected = remember { mutableStateOf(selectedSort == SortTypeEnum.DURATION_SHORTEST_FIRST) }

    val nameAbcSelected = remember { mutableStateOf(selectedSort == SortTypeEnum.NAME_ABC) }
    val nameZyxSelected = remember { mutableStateOf(selectedSort == SortTypeEnum.NAME_ZYX) }

    Box(
        Modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Dialog(
            onDismissRequest = { closeClick() }
        ) {
            Column(
                modifier = modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.secondaryBackground)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.sort_alert_dialog_sort_by),
                    color = MaterialTheme.colors.primaryTextColor,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                CustomRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.sort_alert_dialog_date_newest_first),
                    isSelect = dateNewestSelected,
                    clickAction = {
                        when(screenType) {
                            MainScreenEnums.FLIGHT -> SharedPreferencesManager().setFlightSortPreference(SortTypeEnum.DATE_NEWEST_FIRST)
                            MainScreenEnums.PLACE -> SharedPreferencesManager().setPlaceSortPreference(SortTypeEnum.DATE_NEWEST_FIRST)
                            MainScreenEnums.COUNTRY -> SharedPreferencesManager().setCountrySortPreference(SortTypeEnum.DATE_NEWEST_FIRST)
                        }
                        sortChanged()
                        setSelectValues(
                            dateNewestSelected,
                            dateOldestSelected,
                            durationLongestSelected,
                            durationShortestSelected,
                            nameAbcSelected,
                            nameZyxSelected,
                            SortTypeEnum.DATE_NEWEST_FIRST
                        )
                    }
                )

                CustomRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.sort_alert_dialog_date_oldest_first),
                    isSelect = dateOldestSelected,
                    clickAction = {
                        when(screenType) {
                            MainScreenEnums.FLIGHT -> SharedPreferencesManager().setFlightSortPreference(SortTypeEnum.DATE_OLDEST_FIRST)
                            MainScreenEnums.PLACE -> SharedPreferencesManager().setPlaceSortPreference(SortTypeEnum.DATE_OLDEST_FIRST)
                            MainScreenEnums.COUNTRY -> SharedPreferencesManager().setCountrySortPreference(SortTypeEnum.DATE_OLDEST_FIRST)
                        }
                        sortChanged()
                        setSelectValues(
                            dateNewestSelected,
                            dateOldestSelected,
                            durationLongestSelected,
                            durationShortestSelected,
                            nameAbcSelected,
                            nameZyxSelected,
                            SortTypeEnum.DATE_OLDEST_FIRST
                        )
                    }
                )

                if (showFlightDuration) {
                    CustomRadioButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.sort_alert_dialog_duration_longest_first),
                        isSelect = durationLongestSelected,
                        clickAction = {
                            when(screenType) {
                                MainScreenEnums.FLIGHT -> SharedPreferencesManager().setFlightSortPreference(SortTypeEnum.DURATION_LONGEST_FIRST)
                                MainScreenEnums.PLACE -> SharedPreferencesManager().setPlaceSortPreference(SortTypeEnum.DURATION_LONGEST_FIRST)
                                MainScreenEnums.COUNTRY -> SharedPreferencesManager().setCountrySortPreference(SortTypeEnum.DURATION_LONGEST_FIRST)
                            }
                            sortChanged()
                            setSelectValues(
                                dateNewestSelected,
                                dateOldestSelected,
                                durationLongestSelected,
                                durationShortestSelected,
                                nameAbcSelected,
                                nameZyxSelected,
                                SortTypeEnum.DURATION_LONGEST_FIRST
                            )
                        }
                    )

                    CustomRadioButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.sort_alert_dialog_duration_shortest_first),
                        isSelect = durationShortestSelected,
                        clickAction = {
                            when(screenType) {
                                MainScreenEnums.FLIGHT -> SharedPreferencesManager().setFlightSortPreference(SortTypeEnum.DURATION_SHORTEST_FIRST)
                                MainScreenEnums.PLACE -> SharedPreferencesManager().setPlaceSortPreference(SortTypeEnum.DURATION_SHORTEST_FIRST)
                                MainScreenEnums.COUNTRY -> SharedPreferencesManager().setCountrySortPreference(SortTypeEnum.DURATION_SHORTEST_FIRST)
                            }
                            sortChanged()
                            setSelectValues(
                                dateNewestSelected,
                                dateOldestSelected,
                                durationLongestSelected,
                                durationShortestSelected,
                                nameAbcSelected,
                                nameZyxSelected,
                                SortTypeEnum.DURATION_SHORTEST_FIRST
                            )
                        }
                    )
                }

                if (showName) {
                    CustomRadioButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.sort_alert_dialog_name_abc),
                        isSelect = nameAbcSelected,
                        clickAction = {
                            when(screenType) {
                                MainScreenEnums.FLIGHT -> SharedPreferencesManager().setFlightSortPreference(SortTypeEnum.NAME_ABC)
                                MainScreenEnums.PLACE -> SharedPreferencesManager().setPlaceSortPreference(SortTypeEnum.NAME_ABC)
                                MainScreenEnums.COUNTRY -> SharedPreferencesManager().setCountrySortPreference(SortTypeEnum.NAME_ABC)
                            }
                            sortChanged()
                            setSelectValues(
                                dateNewestSelected,
                                dateOldestSelected,
                                durationLongestSelected,
                                durationShortestSelected,
                                nameAbcSelected,
                                nameZyxSelected,
                                SortTypeEnum.NAME_ABC
                            )
                        }
                    )

                    CustomRadioButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.sort_alert_dialog_name_zyx),
                        isSelect = nameZyxSelected,
                        clickAction = {
                            when(screenType) {
                                MainScreenEnums.FLIGHT -> SharedPreferencesManager().setFlightSortPreference(SortTypeEnum.NAME_ZYX)
                                MainScreenEnums.PLACE -> SharedPreferencesManager().setPlaceSortPreference(SortTypeEnum.NAME_ZYX)
                                MainScreenEnums.COUNTRY -> SharedPreferencesManager().setCountrySortPreference(SortTypeEnum.NAME_ZYX)
                            }
                            sortChanged()
                            setSelectValues(
                                dateNewestSelected,
                                dateOldestSelected,
                                durationLongestSelected,
                                durationShortestSelected,
                                nameAbcSelected,
                                nameZyxSelected,
                                SortTypeEnum.NAME_ZYX
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                CustomButton(
                    clickAction = {
                        closeClick()
                    },
                    text = stringResource(id = R.string.sort_alert_dialog_close_button)
                )
            }
        }
    }
}


private fun setSelectValues(
    dateNewestSelected: MutableState<Boolean>,
    dateOldestSelected: MutableState<Boolean>,
    durationLongestSelected: MutableState<Boolean>,
    durationShortestSelected: MutableState<Boolean>,
    nameAbcSelected: MutableState<Boolean>,
    nameZyxSelected: MutableState<Boolean>,
    selectedSort: SortTypeEnum
) {
    dateNewestSelected.value = selectedSort == SortTypeEnum.DATE_NEWEST_FIRST
    dateOldestSelected.value = selectedSort == SortTypeEnum.DATE_OLDEST_FIRST
    durationLongestSelected.value = selectedSort == SortTypeEnum.DURATION_LONGEST_FIRST
    durationShortestSelected.value = selectedSort == SortTypeEnum.DURATION_SHORTEST_FIRST
    nameAbcSelected.value = selectedSort == SortTypeEnum.NAME_ABC
    nameZyxSelected.value = selectedSort == SortTypeEnum.NAME_ZYX
}