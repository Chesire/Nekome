package com.chesire.nekome.app.search.host.ui

import com.chesire.nekome.app.search.host.domain.SearchGroup

data class UIState(
    val searchText: String,
    val searchGroup: SearchGroup
)
