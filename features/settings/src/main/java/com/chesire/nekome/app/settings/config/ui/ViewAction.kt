package com.chesire.nekome.app.settings.config.ui

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.Theme

sealed interface ViewAction {
    object OnThemeClicked : ViewAction
    data class OnThemeChanged(
        val newTheme: Theme?
    ) : ViewAction

    object OnDefaultHomeScreenClicked : ViewAction
    data class OnDefaultHomeScreenChanged(
        val newHomeScreen: HomeScreenOptions?
    ) : ViewAction

    object OnDefaultSeriesStatusClicked : ViewAction
    data class OnDefaultSeriesStatusChanged(
        val newDefaultSeriesStatus: UserSeriesStatus?
    ) : ViewAction

    data class OnRateSeriesChanged(val newValue: Boolean) : ViewAction
}
