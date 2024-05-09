package com.peterlopusan.traveldiary.sharedPreferences

import android.content.Context
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.data.enums.ContinentsEnum
import com.peterlopusan.traveldiary.data.enums.SortTypeEnum


class SharedPreferencesManager {

    private val sharedPref = MainActivity.instance.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)


    fun resetAllPreferences() {
        setFlightSortPreference(SortTypeEnum.DATE_NEWEST_FIRST)
        setPlaceSortPreference(SortTypeEnum.DATE_NEWEST_FIRST)
        setCountrySortPreference(SortTypeEnum.DATE_NEWEST_FIRST)

        resetFlightFilter()
        resetCountryFilter()
        resetPlaceFilter()
    }


    fun setDisplayMode(setDarkMode: String?) {
        sharedPref.edit().putString(displayModeKey, setDarkMode).apply()
    }

    fun getDisplayMode(): String? {
        //string, not boolean, because boolean doesn't have null option (null for situation when user doesn't have preference)
        return sharedPref.getString(displayModeKey, null)
    }

    fun setLanguage(language: String) {
        sharedPref.edit().putString(languageKey, language).apply()
    }

    fun getLanguage(): String? {
        return sharedPref.getString(languageKey, "")
    }


    fun setFlightSortPreference(sortType: SortTypeEnum) {
        sharedPref.edit().putString(flightSortKey, sortType.name).apply()
    }

    fun getFlightSortPreference(): SortTypeEnum {
        return when (sharedPref.getString(flightSortKey, "")) {
            SortTypeEnum.DATE_OLDEST_FIRST.name -> SortTypeEnum.DATE_OLDEST_FIRST
            SortTypeEnum.DURATION_SHORTEST_FIRST.name -> SortTypeEnum.DURATION_SHORTEST_FIRST
            SortTypeEnum.DURATION_LONGEST_FIRST.name -> SortTypeEnum.DURATION_LONGEST_FIRST
            else -> SortTypeEnum.DATE_NEWEST_FIRST
        }
    }

    fun setPlaceSortPreference(sortType: SortTypeEnum) {
        sharedPref.edit().putString(placeSortKey, sortType.name).apply()
    }

    fun getPlaceSortPreference(): SortTypeEnum {
        return when (sharedPref.getString(placeSortKey, "")) {
            SortTypeEnum.DATE_OLDEST_FIRST.name -> SortTypeEnum.DATE_OLDEST_FIRST
            SortTypeEnum.NAME_ABC.name -> SortTypeEnum.NAME_ABC
            SortTypeEnum.NAME_ZYX.name -> SortTypeEnum.NAME_ZYX
            else -> SortTypeEnum.DATE_NEWEST_FIRST
        }
    }

    fun setCountrySortPreference(sortType: SortTypeEnum) {
        sharedPref.edit().putString(countrySortKey, sortType.name).apply()
    }

    fun getCountrySortPreference(): SortTypeEnum {
        return when (sharedPref.getString(countrySortKey, "")) {
            SortTypeEnum.DATE_OLDEST_FIRST.name -> SortTypeEnum.DATE_OLDEST_FIRST
            SortTypeEnum.NAME_ABC.name -> SortTypeEnum.NAME_ABC
            SortTypeEnum.NAME_ZYX.name -> SortTypeEnum.NAME_ZYX
            else -> SortTypeEnum.DATE_NEWEST_FIRST
        }
    }

    /********************* FILTER FLIGHT *********************/

    fun setFlightSearch(searchText: String) {
        sharedPref.edit().putString(flightFilterSearchKey, searchText).apply()
    }

    fun getFlightSearch(): String {
        return sharedPref.getString(flightFilterSearchKey, "") ?: ""
    }

    fun setFlightDate(date: String, from: Boolean) {
        sharedPref.edit().putString(if (from) flightFilterDateFromKey else flightFilterDateToKey, date).apply()
    }

    fun getFlightDate(from: Boolean): String {
        return sharedPref.getString(if (from) flightFilterDateFromKey else flightFilterDateToKey, "") ?: ""
    }

    fun setFlightDuration(duration: String, from: Boolean) {
        sharedPref.edit().putString(if (from) flightFilterDurationFromKey else flightFilterDurationToKey, duration).apply()
    }

    fun getFlightDuration(from: Boolean): String {
        return sharedPref.getString(if (from) flightFilterDurationFromKey else flightFilterDurationToKey, "") ?: ""
    }

    fun resetFlightFilter() {
        setFlightSearch("")
        setFlightDate("", true)
        setFlightDate("", false)
        setFlightDuration("", true)
        setFlightDuration("", false)
    }

    /********************* FILTER PLACE *********************/

    fun setPlaceSearch(searchText: String) {
        sharedPref.edit().putString(placeFilterSearchKey, searchText).apply()
    }

    fun getPlaceSearch(): String {
        return sharedPref.getString(placeFilterSearchKey, "") ?: ""
    }

    fun setPlaceDate(date: String, from: Boolean) {
        sharedPref.edit().putString(if (from) placeFilterDateFromKey else placeFilterDateToKey, date).apply()
    }

    fun getPlaceDate(from: Boolean): String {
        return sharedPref.getString(if (from) placeFilterDateFromKey else placeFilterDateToKey, "") ?: ""
    }

    fun setIfPlaceCityIsChecked(isChecked: Boolean, city: Boolean) {
        sharedPref.edit().putBoolean(if (city) placeFilterCityKey else placeFilterPlaceKey, isChecked).apply()
    }

    fun getIfPlaceCityIsChecked(city: Boolean): Boolean {
        return sharedPref.getBoolean(if (city) placeFilterCityKey else placeFilterPlaceKey, true)
    }

    fun resetPlaceFilter() {
        setPlaceSearch("")
        setPlaceDate("", true)
        setPlaceDate("", false)
        setIfPlaceCityIsChecked(isChecked = true, city = true)
        setIfPlaceCityIsChecked(isChecked = true, city = false)
    }

    /********************* FILTER COUNTRY *********************/

    fun setCountrySearch(searchText: String) {
        sharedPref.edit().putString(countryFilterSearchKey, searchText).apply()
    }

    fun getCountrySearch(): String {
        return sharedPref.getString(countryFilterSearchKey, "") ?: ""
    }

    fun setIfContinentIsChecked(isChecked: Boolean, continent: ContinentsEnum) {
        when(continent) {
            ContinentsEnum.EUROPE -> sharedPref.edit().putBoolean(countryFilterEuropeCheckedKey ,isChecked).apply()
            ContinentsEnum.AFRICA -> sharedPref.edit().putBoolean(countryFilterAfricaCheckedKey ,isChecked).apply()
            ContinentsEnum.ASIA -> sharedPref.edit().putBoolean(countryFilterAsiaCheckedKey ,isChecked).apply()
            ContinentsEnum.OCEANIA -> sharedPref.edit().putBoolean(countryFilterOceaniaCheckedKey ,isChecked).apply()
            ContinentsEnum.ANTARCTICA -> sharedPref.edit().putBoolean(countryFilterAntarcticaCheckedKey ,isChecked).apply()
            ContinentsEnum.SOUTH_AMERICA -> sharedPref.edit().putBoolean(countryFilterSouthAmericaCheckedKey ,isChecked).apply()
            ContinentsEnum.NORTH_AMERICA -> sharedPref.edit().putBoolean(countryFilterNorthAmericaCheckedKey ,isChecked).apply()
        }
    }

    fun getIfContinentIsChecked(continent: ContinentsEnum): Boolean {
        return when(continent) {
            ContinentsEnum.EUROPE -> sharedPref.getBoolean(countryFilterEuropeCheckedKey, true)
            ContinentsEnum.AFRICA -> sharedPref.getBoolean(countryFilterAfricaCheckedKey, true)
            ContinentsEnum.ASIA -> sharedPref.getBoolean(countryFilterAsiaCheckedKey, true)
            ContinentsEnum.OCEANIA -> sharedPref.getBoolean(countryFilterOceaniaCheckedKey, true)
            ContinentsEnum.ANTARCTICA -> sharedPref.getBoolean(countryFilterAntarcticaCheckedKey, true)
            ContinentsEnum.SOUTH_AMERICA -> sharedPref.getBoolean(countryFilterSouthAmericaCheckedKey, true)
            ContinentsEnum.NORTH_AMERICA -> sharedPref.getBoolean(countryFilterNorthAmericaCheckedKey, true)
        }
    }

    fun resetCountryFilter() {
        setCountrySearch("")
        setIfContinentIsChecked(true, ContinentsEnum.EUROPE)
        setIfContinentIsChecked(true, ContinentsEnum.AFRICA)
        setIfContinentIsChecked(true, ContinentsEnum.ASIA)
        setIfContinentIsChecked(true, ContinentsEnum.OCEANIA)
        setIfContinentIsChecked(true, ContinentsEnum.ANTARCTICA)
        setIfContinentIsChecked(true, ContinentsEnum.SOUTH_AMERICA)
        setIfContinentIsChecked(true, ContinentsEnum.NORTH_AMERICA)
    }
}