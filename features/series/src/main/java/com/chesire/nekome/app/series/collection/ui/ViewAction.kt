package com.chesire.nekome.app.series.collection.ui

sealed interface ViewAction {
    object PerformSeriesRefresh : ViewAction
    data class IncrementSeriesPressed(val series: Series) : ViewAction
    object ErrorSnackbarObserved : ViewAction
}
