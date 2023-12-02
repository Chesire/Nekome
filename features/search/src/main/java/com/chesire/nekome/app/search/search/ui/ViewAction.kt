package com.chesire.nekome.app.search.search.ui

import com.chesire.nekome.app.search.search.core.model.SearchGroup

sealed interface ViewAction {
    data class SearchGroupChanged(val newGroup: SearchGroup) : ViewAction
    data class SearchTextUpdated(val newSearchText: String) : ViewAction
    data object ExecuteSearch : ViewAction
    data class TrackSeries(val model: ResultModel) : ViewAction
    data object ErrorSnackbarObserved : ViewAction
}
