package com.chesire.nekome.server.api

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.server.Resource

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
