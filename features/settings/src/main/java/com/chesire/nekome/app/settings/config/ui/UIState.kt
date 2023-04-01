package com.chesire.nekome.app.settings.config.ui

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.Theme

data class UIState(
    val themeValue: Theme,
    val showThemeDialog: Boolean,
    val defaultHomeValue: HomeScreenOptions,
    val showDefaultHomeDialog: Boolean,
    val defaultSeriesStatusValue: UserSeriesStatus,
    val showDefaultSeriesStatusDialog: Boolean,
    val rateSeriesValue: Boolean
) {
    companion object {
        val default = UIState(
            themeValue = Theme.System,
            showThemeDialog = false,
            defaultHomeValue = HomeScreenOptions.Anime,
            showDefaultHomeDialog = false,
            defaultSeriesStatusValue = UserSeriesStatus.Current,
            showDefaultSeriesStatusDialog = false,
            rateSeriesValue = false
        )
    }
}
