package com.chesire.nekome.app.settings.config.ui

data class UIState(
    val themeValue: String,
    val rateSeriesValue: Boolean
) {
    companion object {
        val default = UIState(
            themeValue = "",
            rateSeriesValue = false
        )
    }
}
