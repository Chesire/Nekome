package com.chesire.nekome.app.series.item.ui

import androidx.annotation.StringRes
import com.chesire.nekome.core.flags.UserSeriesStatus

data class UIState(
    val id: Int,
    val title: String,
    val subtitle: String,
    val seriesStatus: UserSeriesStatus,
    val progress: Int,
    val length: String,
    val rating: Int,
    val isSendingData: Boolean,
    val errorSnackbar: SnackbarData?
) {
    companion object {
        val default = UIState(
            id = 0,
            title = "",
            subtitle = "",
            seriesStatus = UserSeriesStatus.Unknown,
            progress = 0,
            length = "-",
            rating = 0,
            isSendingData = false,
            errorSnackbar = null
        )
    }
}

data class SnackbarData(
    @StringRes val stringRes: Int,
    val formatText: Any = ""
)
