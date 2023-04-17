package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.trending.TrendingDomain
import com.chesire.nekome.datasource.trending.remote.TrendingApi
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.trending.dto.TrendingResponseDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import retrofit2.Response

/**
 * Implementation of the [TrendingApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuTrending @Inject constructor(
    private val trendingService: KitsuTrendingService,
    private val map: TrendingItemDtoMapper
) : TrendingApi {

    override suspend fun getTrendingAnime(): Result<List<TrendingDomain>, ErrorDomain> {
        return try {
            parseResponse(trendingService.getTrendingAnimeAsync())
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    override suspend fun getTrendingManga(): Result<List<TrendingDomain>, ErrorDomain> {
        return try {
            parseResponse(trendingService.getTrendingMangaAsync())
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    private fun parseResponse(
        response: Response<TrendingResponseDto>
    ): Result<List<TrendingDomain>, ErrorDomain> {
        return if (response.isSuccessful) {
            response.body()?.let { trending ->
                Ok(trending.data.map { map.toTrendingDomain(it) })
            } ?: Err(ErrorDomain.emptyResponse)
        } else {
            Err(response.asError())
        }
    }
}
