package com.chesire.nekome.app.settings.config.ui

import com.chesire.nekome.core.preferences.flags.Theme

data class UIState(
    val themeValue: Theme,
    val showThemeDialog: Boolean,
    val rateSeriesValue: Boolean
) {
    companion object {
        val default = UIState(
            themeValue = Theme.System,
            showThemeDialog = false,
            rateSeriesValue = false
        )
    }
}
