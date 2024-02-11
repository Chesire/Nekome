package com.chesire.nekome.core.compose.theme

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Used to set the theme for a given composable, defaulting to the system theme
 * @param theme an int preferably defined by the enum [Theme][com.chesire.nekome.core.preferences.flags.Theme]
 * @param isDynamicColor a boolean that sets whether the given theme will be dynamic or not
 * @param content a composable you want to set the given theme to
 */
@Composable
fun NekomeTheme(
    theme: Int,
    isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val isSystemDarkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && isSystemDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !isSystemDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        theme == 1 -> LightColorPalette
        theme == 2 -> DarkColorPalette
        theme == 5 -> BlackColorPalette
        else -> LightColorPalette
    }
    systemUiController.apply {
        setNavigationBarColor(color = colorScheme.surface)
        setStatusBarColor(color = colorScheme.background)
    }

    Log.d(
        "Nekome",
        "Is system in dark theme? [$isSystemDarkTheme], is using dynamic color? [$isDynamicColor]"
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NekomeTypography,
        shapes = NekomeShapes,
        content = content
    )
}
