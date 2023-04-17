package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.search.SearchDomain
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.search.dto.SearchResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import retrofit2.Response

/**
 * Implementation of the [SearchApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuSearch @Inject constructor(
    private val searchService: KitsuSearchService,
    private val map: SearchItemDtoMapper
) : SearchApi {

    override suspend fun searchForAnime(title: String): Result<List<SearchDomain>, ErrorDomain> {
        return try {
            parseResponse(searchService.searchForAnimeAsync(title))
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    override suspend fun searchForManga(title: String): Result<List<SearchDomain>, ErrorDomain> {
        return try {
            parseResponse(searchService.searchForMangaAsync(title))
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    private fun parseResponse(response: Response<SearchResponseDto>): Result<List<SearchDomain>, ErrorDomain> {
        return if (response.isSuccessful) {
            response.body()?.let { search ->
                Ok(search.data.map { map.toSearchDomain(it) })
            } ?: Err(ErrorDomain.emptyResponse)
        } else {
            Err(response.asError())
        }
    }
}
