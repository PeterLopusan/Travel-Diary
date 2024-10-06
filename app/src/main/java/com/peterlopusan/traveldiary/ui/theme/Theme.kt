package com.peterlopusan.traveldiary.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = PrimaryBackgroundDark,
    onBackground = SecondaryBackgroundDark,
    onPrimary = PrimaryTextLight,
    onSecondary = SecondaryTextDark
)

private val LightColorScheme = lightColorScheme(
    background = PrimaryBackgroundLight,
    onBackground = SecondaryBackgroundLight,
    onPrimary = PrimaryTextDark,
    onSecondary = SecondaryTextLight
)

@Composable
fun TravelDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val customColorsPalette = if (darkTheme) DarkCustomColorsPalette else LightCustomColorsPalette
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context = LocalContext.current)
            else dynamicLightColorScheme(context = LocalContext.current)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalTravelDiaryColors provides customColorsPalette) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}


