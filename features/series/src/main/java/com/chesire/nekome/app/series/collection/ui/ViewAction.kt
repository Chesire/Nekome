package com.chesire.nekome.app.series.collection.ui

import com.chesire.nekome.datasource.series.SeriesDomain

sealed interface ViewAction {
    object PerformSeriesRefresh : ViewAction
    data class IncrementSeriesPressed(val seriesDomain: SeriesDomain) : ViewAction
}
