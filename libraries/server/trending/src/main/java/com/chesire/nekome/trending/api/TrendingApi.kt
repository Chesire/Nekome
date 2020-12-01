package com.chesire.nekome.trending.api

import com.chesire.nekome.core.Resource

/**
 * Methods relating to getting information about trending topics.
 */
interface TrendingApi {
    /**
     * Retrieves the current trending anime.
     */
    suspend fun getTrendingAnime(): Resource<List<TrendingDomain>>

    /**
     * Retrieves the current trending manga.
     */
    suspend fun getTrendingManga(): Resource<List<TrendingDomain>>
}
