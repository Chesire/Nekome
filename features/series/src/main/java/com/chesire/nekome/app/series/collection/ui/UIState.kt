package com.chesire.nekome.app.series.collection.ui

import androidx.annotation.StringRes

data class UIState(
    val models: List<Series>,
    val isRefreshing: Boolean,
    val ratingDialog: Rating?,
    val errorSnackbar: SnackbarData?
)

data class Series(
    val userId: Int,
    val title: String,
    val posterImageUrl: String,
    val subtype: String,
    val progress: String,
    val startDate: String,
    val endDate: String,
    val rating: Int,
    val showPlusOne: Boolean,
    val isUpdating: Boolean
)

data class Rating(
    val series: Series,
    val show: Boolean
)

data class SnackbarData(
    @StringRes val stringRes: Int,
    val formatText: Any = ""
)
