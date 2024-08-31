package com.chesire.nekome.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            NekomeTheme(theme = themeState.value) {
                MainActivityScreen()
            }
        }
    }

}
