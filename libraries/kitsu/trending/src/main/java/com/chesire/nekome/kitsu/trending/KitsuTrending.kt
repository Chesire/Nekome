package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.trending.api.TrendingApi
import com.chesire.nekome.trending.api.TrendingEntity
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of the [TrendingApi] for usage with the Kitsu API.
 */
class KitsuTrending @Inject constructor(
    private val trendingService: KitsuTrendingService,
    private val map: EntityMapper<KitsuTrendingEntity, TrendingEntity>
) : TrendingApi {

    override suspend fun getTrendingAnime(): Resource<List<TrendingEntity>> {
        return try {
            parseResponse(trendingService.getTrendingAnimeAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun getTrendingManga(): Resource<List<TrendingEntity>> {
        return try {
            parseResponse(trendingService.getTrendingMangaAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(response: Response<TrendingData>): Resource<List<TrendingEntity>> {
        return if (response.isSuccessful) {
            response.body()?.let { trending ->
                Resource.Success(trending.data.map { map.from(it) })
            } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
