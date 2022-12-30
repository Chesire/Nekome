package com.chesire.nekome.app.search.host.ui

import com.chesire.nekome.app.search.host.core.model.SearchGroup

sealed interface ViewAction {
    data class SearchGroupChanged(val newGroup: SearchGroup) : ViewAction
    data class SearchTextUpdated(val newSearchText: String) : ViewAction
    object ExecuteSearch : ViewAction
}
