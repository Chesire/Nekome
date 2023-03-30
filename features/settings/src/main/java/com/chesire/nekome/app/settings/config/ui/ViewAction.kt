package com.chesire.nekome.app.settings.config.ui

import com.chesire.nekome.core.preferences.flags.Theme

sealed interface ViewAction {
    data class OnRateSeriesChanged(val newValue: Boolean) : ViewAction
    object OnThemeClicked : ViewAction
    data class OnThemeChanged(val newTheme: Theme?) : ViewAction
}
