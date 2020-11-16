package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.trending.api.TrendingApi
import com.chesire.nekome.trending.api.TrendingEntity
import com.chesire.nekome.trending.api.TrendingEntityMapper
import retrofit2.Response

class KitsuTrending(
    private val trendingService: KitsuTrendingService,
    private val mapper: TrendingEntityMapper<KitsuTrendingEntity>
) : TrendingApi {

    override suspend fun trendingAnime(): Resource<List<TrendingEntity>> {
        return try {
            parseResponse(trendingService.getTrendingAnimeAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun trendingManga(): Resource<List<TrendingEntity>> {
        return try {
            parseResponse(trendingService.getTrendingMangaAsync())
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(response: Response<TrendingData>): Resource<List<TrendingEntity>> {
        return if (response.isSuccessful) {
            response.body()?.let { trending ->
                Resource.Success(trending.data.map { mapper.mapToTrendingEntity(it) })
            } ?: Resource.Error.emptyResponse()
        } else {
            response.asError()
        }
    }
}
