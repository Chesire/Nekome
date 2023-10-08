package com.chesire.nekome.feature.serieswidget.ui

data class UIState(
    val series: List<Series> = emptyList()
)

data class Series(
    val userId: Int,
    val title: String,
    val progress: String,
    val isUpdating: Boolean
)
