package com.chesire.nekome.datasource.trending.remote

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.trending.TrendingDomain

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
