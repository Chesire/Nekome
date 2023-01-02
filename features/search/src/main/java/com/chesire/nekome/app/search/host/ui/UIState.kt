package com.chesire.nekome.app.search.host.ui

import androidx.annotation.StringRes
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.app.search.host.core.model.SearchGroup

data class UIState(
    val searchText: String,
    val isSearchTextError: Boolean,
    val searchGroup: SearchGroup,
    val isSearching: Boolean,
    @StringRes val errorSnackbarMessage: Int?,
    val navigateScreenEvent: NavigationData?
) {
    companion object {
        val Default
            get() = UIState(
                searchText = "",
                isSearchTextError = false,
                searchGroup = SearchGroup.Anime,
                isSearching = false,
                errorSnackbarMessage = null,
                navigateScreenEvent = null
            )
    }
}

data class NavigationData(
    val searchTerm: String,
    val searchResults: List<SearchModel>
)
