package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.search.SearchDomain
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchSeriesUseCase @Inject constructor(private val searchApi: SearchApi) {

    suspend operator fun invoke(
        title: String,
        group: SearchGroup
    ): Result<List<SearchModel>, SearchFailureReason> {
        if (isTitleInvalid(title)) {
            return Err(SearchFailureReason.InvalidTitle)
        }

        val result = withContext(Dispatchers.IO) {
            when (group) {
                SearchGroup.Anime -> searchApi.searchForAnime(title)
                SearchGroup.Manga -> searchApi.searchForManga(title)
            }
        }

        return if (result is Resource.Success) {
            if (result.data.isEmpty()) {
                Err(SearchFailureReason.NoSeriesFound)
            } else {
                Ok(result.data.map { it.toSearchModel() })
            }
        } else {
            Err(SearchFailureReason.NetworkError)
        }
    }

    private fun isTitleInvalid(title: String): Boolean {
        return title.isBlank()
    }

    private fun SearchDomain.toSearchModel(): SearchModel {
        return SearchModel(
            id = id,
            type = type,
            synopsis = synopsis,
            canonicalTitle = canonicalTitle,
            subtype = subtype,
            posterImage = posterImage
        )
    }
}

sealed interface SearchFailureReason {
    object InvalidTitle : SearchFailureReason
    object NoSeriesFound : SearchFailureReason
    object NetworkError : SearchFailureReason
}
