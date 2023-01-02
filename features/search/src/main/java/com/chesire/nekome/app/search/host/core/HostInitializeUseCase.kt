package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.app.search.host.data.HostPreferences
import javax.inject.Inject
import timber.log.Timber

class HostInitializeUseCase @Inject constructor(private val hostPreferences: HostPreferences) {

    operator fun invoke() = InitializeArgs(initialGroup = retrieveInitialGroup())

    private fun retrieveInitialGroup(): SearchGroup {
        val lastGroup = hostPreferences.lastSearchGroup

        return if (lastGroup.isBlank()) {
            SearchGroup.Anime
        } else {
            try {
                SearchGroup.valueOf(hostPreferences.lastSearchGroup)
            } catch (ex: IllegalArgumentException) {
                Timber.w("Failed to parse the search group - [$lastGroup]")
                SearchGroup.Anime
            }
        }
    }
}

data class InitializeArgs(
    val initialGroup: SearchGroup
)
