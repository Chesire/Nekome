package com.chesire.nekome.app.series.item.ui

import androidx.annotation.StringRes
import com.chesire.nekome.core.flags.UserSeriesStatus

data class UIState(
    val id: Int,
    val title: String,
    val subtitle: String,
    val possibleSeriesStatus: List<UserSeriesStatus>,
    val seriesStatus: UserSeriesStatus,
    val progress: String,
    val length: String,
    val rating: Int,
    val isSendingData: Boolean,
    val finishScreen: Boolean,
    val deleteDialog: Delete?,
    val errorSnackbar: SnackbarData?
) {
    companion object {
        val default = UIState(
            id = 0,
            title = "",
            subtitle = "",
            possibleSeriesStatus = listOf(
                UserSeriesStatus.Current,
                UserSeriesStatus.Completed,
                UserSeriesStatus.Dropped,
                UserSeriesStatus.OnHold,
                UserSeriesStatus.Planned
            ),
            seriesStatus = UserSeriesStatus.Unknown,
            progress = "0",
            length = "-",
            rating = 0,
            finishScreen = false,
            isSendingData = false,
            deleteDialog = null,
            errorSnackbar = null
        )
    }
}

data class Delete(
    val show: Boolean,
    val title: String
)

data class SnackbarData(
    @StringRes val stringRes: Int,
    val formatText: Any = ""
)
