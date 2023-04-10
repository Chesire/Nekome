package com.chesire.nekome.core.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = LightBlue219
)

private val LightColorPalette = lightColors(
    primary = LightGray607,
    primaryVariant = DarkGray455,
    secondary = LightBlue42A
)

@Composable
fun NekomeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    systemUiController.apply {
        setSystemBarsColor(color = Color.Transparent)
        setNavigationBarColor(color = Color.Transparent)
        setStatusBarColor(color = Color.Transparent)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
