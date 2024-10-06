package com.peterlopusan.traveldiary.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val PrimaryBackgroundDark = Color(0xFF1D1D1B)
val PrimaryBackgroundLight = Color(0xFFFFFFFF)

val SecondaryBackgroundDark = Color(0xFF464643)
val SecondaryBackgroundLight = Color(0xFFCAC6C6)

val PrimaryTextDark = Color(0xFF000000)
val PrimaryTextLight = Color(0xFFFFFFFF)

val SecondaryTextDark = Color(0xFF5B5C61)
val SecondaryTextLight = Color(0xFFA0A0A0)

val ConfirmColor = Color(0xFF13A538)
val CancelColor = Color(0xFFD01414)


val VisitedCountryColor = Color(0x99F43636)
val MyCountryColor = Color(0xAAFFE500)

@Immutable
data class TravelDiaryColorsPalette(
    val primaryBackground: Color = Color.Unspecified,
    val secondaryBackground: Color = Color.Unspecified,
    val primaryTextColor: Color = Color.Unspecified,
    val secondaryTextColor: Color = Color.Unspecified
)


val LightCustomColorsPalette = TravelDiaryColorsPalette(
    primaryBackground = PrimaryBackgroundLight,
    secondaryBackground = SecondaryBackgroundLight,
    primaryTextColor = PrimaryTextDark,
    secondaryTextColor = SecondaryTextDark
)

val DarkCustomColorsPalette = TravelDiaryColorsPalette(
    primaryBackground = PrimaryBackgroundDark,
    secondaryBackground = SecondaryBackgroundDark,
    primaryTextColor = PrimaryTextLight,
    secondaryTextColor = SecondaryTextLight
)

val LocalTravelDiaryColors = staticCompositionLocalOf { TravelDiaryColorsPalette() }