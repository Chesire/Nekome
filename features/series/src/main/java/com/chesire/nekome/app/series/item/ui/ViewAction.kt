package com.chesire.nekome.app.series.item.ui

import com.chesire.nekome.core.flags.UserSeriesStatus

sealed interface ViewAction {
    data class SeriesStatusChanged(val newSeriesStatus: UserSeriesStatus) : ViewAction
    data class ProgressChanged(val newProgress: Int) : ViewAction
    data class RatingChanged(val newRating: Int) : ViewAction
    object CancelPressed : ViewAction
    object ConfirmPressed : ViewAction
    object SnackbarObserved : ViewAction
}
