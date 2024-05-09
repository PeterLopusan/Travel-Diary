package com.peterlopusan.traveldiary.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColors = darkColors(
    background = PrimaryBackgroundDark,
    onBackground = SecondaryBackgroundDark,
    onPrimary = PrimaryTextLight,
    onSecondary = SecondaryTextDark
)

private val LightColors = lightColors(
    background = PrimaryBackgroundLight,
    onBackground = SecondaryBackgroundLight,
    onPrimary = PrimaryTextDark,
    onSecondary = SecondaryTextLight
)

@Composable
fun TravelDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    //todo fix dark mode

    if (!darkTheme) {
        systemUiController.setStatusBarColor(Color.Black)
    }

    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

// my custom colors names
@get:Composable
val Colors.primaryBackground: Color
    get() = if (isLight) PrimaryBackgroundLight else PrimaryBackgroundDark

val Colors.secondaryBackground: Color
    get() = if (isLight) SecondaryBackgroundLight else SecondaryBackgroundDark

val Colors.primaryTextColor: Color
    get() = if (isLight) PrimaryTextDark else PrimaryTextLight

val Colors.secondaryTextColor: Color
    get() = if (isLight) SecondaryTextDark else SecondaryTextLight


