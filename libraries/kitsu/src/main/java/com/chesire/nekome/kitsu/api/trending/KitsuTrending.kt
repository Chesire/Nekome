package com.chesire.nekome.kitsu.api.trending

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.TrendingApi
import javax.inject.Inject

/**
 * Provides an implementation of [TrendingApi] to interact with [KitsuTrendingService] to pull
 * trending data.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuTrending @Inject constructor(
    private val trendingService: KitsuTrendingService
) : TrendingApi {
    override suspend fun trendingAnime(): Resource<List<SeriesModel>> {
        return try {
            trendingService.getTrendingAnimeAsync().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun trendingManga(): Resource<List<SeriesModel>> {
        return try {
            trendingService.getTrendingMangaAsync().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }
}
