package com.chesire.nekome.app.series.item.ui

import com.chesire.nekome.core.flags.UserSeriesStatus

sealed interface ViewAction {
    data class OnDeleteResult(val result: Boolean) : ViewAction
    data class ProgressChanged(val newProgress: String) : ViewAction
    data class RatingChanged(val newRating: Float) : ViewAction
    data class SeriesStatusChanged(val newSeriesStatus: UserSeriesStatus) : ViewAction
    object ConfirmPressed : ViewAction
    object DeletePressed : ViewAction
    object SnackbarObserved : ViewAction
    object FinishScreenObserved : ViewAction
}
