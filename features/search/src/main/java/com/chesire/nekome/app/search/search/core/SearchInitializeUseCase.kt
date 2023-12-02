package com.chesire.nekome.app.search.search.core

import com.chesire.nekome.app.search.search.core.model.SearchGroup
import com.chesire.nekome.app.search.search.data.SearchPreferences
import javax.inject.Inject
import timber.log.Timber

class SearchInitializeUseCase @Inject constructor(
    private val searchPreferences: SearchPreferences
) {

    operator fun invoke() = InitializeArgs(initialGroup = retrieveInitialGroup())

    private fun retrieveInitialGroup(): SearchGroup {
        val lastGroup = searchPreferences.lastSearchGroup

        return if (lastGroup.isBlank()) {
            SearchGroup.Anime
        } else {
            try {
                SearchGroup.valueOf(searchPreferences.lastSearchGroup)
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
