package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.app.search.host.data.HostPreferences
import com.chesire.nekome.app.search.host.domain.SearchGroup
import javax.inject.Inject

class HostInitializeUseCase @Inject constructor(private val hostPreferences: HostPreferences) {

    operator fun invoke(): InitializeArgs {
        return InitializeArgs(initialGroup = retrieveInitialGroup())
    }

    private fun retrieveInitialGroup(): SearchGroup {
        val lastGroup = hostPreferences.lastSearchGroup

        return if (lastGroup.isBlank()) {
            SearchGroup.Anime
        } else {
            try {
                SearchGroup.valueOf(hostPreferences.lastSearchGroup)
            } catch (ex: IllegalArgumentException) {
                SearchGroup.Anime
            }
        }
    }
}

data class InitializeArgs(
    val initialGroup: SearchGroup
)
