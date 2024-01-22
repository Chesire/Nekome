package com.chesire.nekome.app.series.collection.ui

import androidx.annotation.StringRes
import com.chesire.nekome.core.preferences.flags.SortOption
import com.chesire.nekome.resources.StringResource

data class UIState(
    @StringRes val screenTitle: Int,
    val isInitializing: Boolean,
    val models: List<Series>,
    val isRefreshing: Boolean,
    val ratingDialog: Rating?,
    val errorSnackbar: SnackbarData?,
    val seriesDetails: SeriesDetails?,
    val sortDialog: Sort,
    val filterDialog: Filter
) {
    companion object {
        val default = UIState(
            screenTitle = StringResource.nav_anime,
            isInitializing = true,
            models = emptyList(),
            isRefreshing = false,
            ratingDialog = null,
            errorSnackbar = null,
            seriesDetails = null,
            sortDialog = Sort(
                show = false,
                currentSort = SortOption.Default,
                sortOptions = listOf(
                    SortOption.Default,
                    SortOption.Title,
                    SortOption.StartDate,
                    SortOption.EndDate,
                    SortOption.Rating
                )
            ),
            filterDialog = Filter(
                show = false,
                filterOptions = emptyList()
            )
        )
    }
}

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
    val show: Boolean,
    val seriesId: Int
)

data class Sort(
    val show: Boolean,
    val currentSort: SortOption,
    val sortOptions: List<SortOption>
)

data class Filter(
    val show: Boolean,
    val filterOptions: List<FilterOption>
)
