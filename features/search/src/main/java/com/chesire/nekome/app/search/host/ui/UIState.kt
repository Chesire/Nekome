package com.chesire.nekome.app.search.host.ui

import androidx.annotation.StringRes
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.ImageModel

data class UIState(
    val searchText: String,
    val isSearchTextError: Boolean,
    val searchGroup: SearchGroup,
    val isSearching: Boolean,
    val resultModels: List<ResultModel>,
    val errorSnackbar: SnackbarData?
) {
    companion object {
        val Default
            get() = UIState(
                searchText = "",
                isSearchTextError = false,
                searchGroup = SearchGroup.Anime,
                isSearching = false,
                resultModels = emptyList(),
                errorSnackbar = null
            )
    }
}

data class ResultModel(
    val id: Int,
    val type: SeriesType,
    val synopsis: String,
    val canonicalTitle: String,
    val subtype: String,
    val posterImage: ImageModel,
    val canTrack: Boolean,
    val isTracking: Boolean
)

data class SnackbarData(
    @StringRes val stringRes: Int,
    val formatText: String = ""
)
