package com.chesire.nekome.app.series.item.ui

import com.chesire.nekome.core.flags.UserSeriesStatus

data class UIState(
    val id: Int,
    val title: String,
    val subtitle: String,
    val seriesStatus: UserSeriesStatus,
    val progress: Int,
    val length: String,
    val rating: Int
) {
    companion object {
        val default = UIState(
            id = 0,
            title = "",
            subtitle = "",
            seriesStatus = UserSeriesStatus.Unknown,
            progress = 0,
            length = "-",
            rating = 0
        )
    }
}
