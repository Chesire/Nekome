package com.chesire.nekome.app.series.collection.ui

import com.chesire.nekome.core.preferences.flags.SortOption

sealed interface ViewAction {
    object PerformSeriesRefresh : ViewAction
    data class SeriesPressed(val series: Series) : ViewAction
    object SeriesNavigationObserved : ViewAction
    data class IncrementSeriesPressed(val series: Series) : ViewAction
    data class IncrementSeriesWithRating(val series: Series, val rating: Int?) : ViewAction
    object SortPressed : ViewAction
    data class PerformSort(val option: SortOption?) : ViewAction
    object FilterPressed : ViewAction
    data class PerformFilter(val filters: List<FilterOption>?) : ViewAction
    object ErrorSnackbarObserved : ViewAction
}
