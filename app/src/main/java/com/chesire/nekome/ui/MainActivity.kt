package com.chesire.nekome.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.flags.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@LogLifecykle
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSettings: ApplicationPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeState = appSettings.theme.collectAsState(initial = Theme.System)
            val (theme, isDynamicColor) = generateTheme(
                theme = themeState.value,
                isSystemDarkMode = isSystemInDarkTheme()
            )
            NekomeTheme(
                theme = theme.value,
                isDynamicColor = isDynamicColor
            ) {
                MainActivityScreen()
            }
        }
    }

    /**
     * Returns a [Pair] of (dark mode enabled) to (dynamic color enabled).
     */
    private fun generateTheme(theme: Theme, isSystemDarkMode: Boolean): Pair<Theme, Boolean> {
        val systemTheme = if(isSystemDarkMode) Theme.Dark else Theme.Light
        return when (theme) {
            Theme.System -> systemTheme to true
            Theme.Dark -> Theme.Dark to false
            Theme.Light -> Theme.Light to false
            Theme.Black -> Theme.Black to false
            Theme.DynamicDark -> Theme.DynamicDark to true
            Theme.DynamicLight -> Theme.DynamicLight to true
        }
    }
}
