package com.chesire.nekome.app.search.search.core

import com.chesire.nekome.app.search.search.core.model.SearchGroup
import com.chesire.nekome.app.search.search.data.SearchPreferences
import javax.inject.Inject

class RememberSearchGroupUseCase @Inject constructor(
    private val searchPreferences: SearchPreferences
) {

    operator fun invoke(newSearchGroup: SearchGroup) {
        searchPreferences.lastSearchGroup = newSearchGroup.name
    }
}
