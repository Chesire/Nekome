package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.trending.TrendingDomain
import com.chesire.nekome.datasource.trending.remote.TrendingApi
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.kitsu.trending.dto.TrendingResponseDto
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

    override suspend fun getTrendingAnime(): Resource<List<TrendingDomain>> {
        return try {
            parseResponse(trendingService.getTrendingAnimeAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun getTrendingManga(): Resource<List<TrendingDomain>> {
        return try {
            parseResponse(trendingService.getTrendingMangaAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(
        response: Response<TrendingResponseDto>
    ): Resource<List<TrendingDomain>> {
        return if (response.isSuccessful) {
            response.body()?.let { trending ->
                Resource.Success(trending.data.map { map.toTrendingDomain(it) })
            } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
