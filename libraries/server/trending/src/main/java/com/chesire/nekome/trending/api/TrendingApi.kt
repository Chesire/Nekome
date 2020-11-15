package com.chesire.nekome.trending.api

import com.chesire.nekome.core.Resource

/**
 * Methods relating to getting information about trending topics.
 */
interface TrendingApi {
    /**
     * Gets the current trending anime.
     */
    suspend fun trendingAnime(): Resource<List<TrendingEntity>>

    /**
     * Gets the current trending manga.
     */
    suspend fun trendingManga(): Resource<List<TrendingEntity>>
}
