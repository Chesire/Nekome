package com.chesire.nekome.app.settings.config.ui

import androidx.annotation.StringRes
import com.chesire.nekome.core.preferences.flags.Theme

data class UIState(
    @StringRes val themeStringDisplay: Int,
    val rateSeriesValue: Boolean
) {
    companion object {
        val default = UIState(
            themeStringDisplay = Theme.System.stringId,
            rateSeriesValue = false
        )
    }
}
