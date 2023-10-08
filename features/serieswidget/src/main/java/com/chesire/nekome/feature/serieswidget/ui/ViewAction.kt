package com.chesire.nekome.feature.serieswidget.ui

sealed interface ViewAction {

    data class UpdateSeries(val id: String) : ViewAction
}
