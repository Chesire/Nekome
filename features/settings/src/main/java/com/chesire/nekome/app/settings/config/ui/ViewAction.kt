package com.chesire.nekome.app.settings.config.ui

sealed interface ViewAction {
    data class OnRateSeriesChanged(val newValue: Boolean) : ViewAction
}
