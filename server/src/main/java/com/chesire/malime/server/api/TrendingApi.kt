package com.chesire.malime.server.api

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.server.Resource

/**
 * Methods relating to getting information about trending topics.
 */
interface TrendingApi {
    /**
     * Gets the current trending anime.
     */
    suspend fun trendingAnime(): Resource<List<SeriesModel>>

    /**
     * Gets the current trending manga.
     */
    suspend fun trendingManga(): Resource<List<SeriesModel>>
}
