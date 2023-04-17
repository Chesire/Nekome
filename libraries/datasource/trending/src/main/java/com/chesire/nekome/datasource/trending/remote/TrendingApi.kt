package com.chesire.nekome.datasource.trending.remote

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.trending.TrendingDomain
import com.github.michaelbull.result.Result

/**
 * Methods relating to getting information about trending topics.
 */
interface TrendingApi {

    /**
     * Retrieves the current trending anime.
     */
    suspend fun getTrendingAnime(): Result<List<TrendingDomain>, ErrorDomain>

    /**
     * Retrieves the current trending manga.
     */
    suspend fun getTrendingManga(): Result<List<TrendingDomain>, ErrorDomain>
}
