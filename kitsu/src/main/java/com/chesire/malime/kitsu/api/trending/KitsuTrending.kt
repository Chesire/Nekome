package com.chesire.malime.kitsu.api.trending

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.parse
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.TrendingApi
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
