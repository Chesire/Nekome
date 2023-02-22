package com.chesire.nekome.app.series.collection.ui

import androidx.annotation.StringRes
import com.chesire.nekome.core.flags.SortOption

data class UIState(
    val models: List<Series>,
    val isRefreshing: Boolean,
    val ratingDialog: Rating?,
    val errorSnackbar: SnackbarData?,
    val seriesDetails: SeriesDetails?,
    val sortDialog: Sort,
    val filterDialog: Filter
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

data class SeriesDetails(
    val userId: Int, // <-- Set from the Series object
    val title: String, // <-- Set from the Series object
    val subtitle: String, // <-- Build from the Series object
    val userSeriesStatus: String, // ?? maybe UserSeriesStatus, <-- Retrieve from DB
    val progress: Int, // <-- Retrieve from the DB?
    val maxProgress: String, // <-- Retrieve from the DB?
    val rating: Int // <-- Retrieve from the DB?
)

data class Sort(
    val show: Boolean,
    val currentSort: SortOption,
    val sortOptions: List<SortOption>
)

data class Filter(
    val show: Boolean
)
