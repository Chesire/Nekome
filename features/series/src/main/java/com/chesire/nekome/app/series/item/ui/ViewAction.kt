package com.chesire.nekome.app.series.item.ui

import com.chesire.nekome.core.flags.UserSeriesStatus

sealed interface ViewAction {
    data class LinkPressed(val link: Link) : ViewAction
    data class OnLinkResult(val linkDialogResult: LinkDialogResult?) : ViewAction
    data class OnDeleteResult(val result: Boolean) : ViewAction
    data class ProgressChanged(val newProgress: String) : ViewAction
    data class RatingChanged(val newRating: Float) : ViewAction
    data class SeriesStatusChanged(val newSeriesStatus: UserSeriesStatus) : ViewAction
    object ConfirmPressed : ViewAction
    object DeletePressed : ViewAction
    object SnackbarObserved : ViewAction
    object FinishScreenObserved : ViewAction
}

data class LinkDialogResult(
    val link: Link,
    val titleText: String,
    val linkText: String
)
