package com.chesire.core.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkGray333,
    primaryVariant = DarkGray252,
    secondary = LightBlue219,
    secondaryVariant = LightBlue42A,
    onSurface = LightBlue219
)

private val LightColorPalette = lightColors(
    primary = LightGray607,
    primaryVariant = DarkGray455,
    secondary = LightBlue42A
)

@Composable
fun NekomeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
