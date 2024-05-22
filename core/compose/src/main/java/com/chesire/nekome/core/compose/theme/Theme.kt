package com.chesire.nekome.core.compose.theme

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.chesire.nekome.core.preferences.flags.Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Used to set the theme for your app.
 *
 * If the device is incompatible with a given dynamic theme
 * due to the api level being below [Build.VERSION_CODES.S], they will default to their
 * non-dynamic versions
 * @param theme a [Theme] enum, this defaults to [Theme.System].
 * @param content a composable you want to set the given theme to.
 */
@Composable
fun NekomeTheme(
    theme: Theme = Theme.System,
    content: @Composable () -> Unit,
) {
    val dynamicCompatible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val isSystemDarkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    val dynamicDarkColorPalette =
        if (dynamicCompatible) dynamicDarkColorScheme(LocalContext.current) else DarkColorPalette

    val dynamicLightColorPalette =
        if (dynamicCompatible) dynamicLightColorScheme(LocalContext.current) else LightColorPalette

    val systemTheme = if(isSystemDarkTheme) dynamicDarkColorPalette  else  dynamicLightColorPalette
    val colorScheme = when (theme) {
        Theme.System -> systemTheme
        Theme.Dark -> DarkColorPalette
        Theme.Light -> LightColorPalette
        Theme.Black -> BlackColorPalette
        Theme.DynamicDark -> dynamicDarkColorPalette
        Theme.DynamicLight -> dynamicLightColorPalette
        else -> systemTheme
    }

    systemUiController.apply {
        setNavigationBarColor(color = colorScheme.surface)
        setStatusBarColor(color = colorScheme.background)
    }

    Log.d(
        "Nekome",
        "Is system in dark theme? [$isSystemDarkTheme]\n\t " +
                "Dynamic compatible device? [$dynamicCompatible]\n\t" +
                "selected theme : [${theme.name}] "
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NekomeTypography,
        shapes = NekomeShapes,
        content = content
    )
}
