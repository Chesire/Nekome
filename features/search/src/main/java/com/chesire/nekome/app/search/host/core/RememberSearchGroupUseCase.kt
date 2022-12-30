package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.app.search.host.data.HostPreferences
import javax.inject.Inject

class RememberSearchGroupUseCase @Inject constructor(
    private val hostPreferences: HostPreferences
) {

    operator fun invoke(newSearchGroup: SearchGroup) {
        hostPreferences.lastSearchGroup = newSearchGroup.name
    }
}
