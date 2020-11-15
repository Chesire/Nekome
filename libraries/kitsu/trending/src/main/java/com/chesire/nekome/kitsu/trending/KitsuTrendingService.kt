package com.chesire.nekome.kitsu.trending

import retrofit2.Response
import retrofit2.http.GET

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related to trending.
 */
interface KitsuTrendingService {
    /**
     * Gets the top trending Anime from Kitsu.
     */
    @GET("api/edge/trending/anime")
    suspend fun getTrendingAnimeAsync(): Response<TrendingData>

    /**
     * Gets the top trending Manga from Kitsu.
     */
    @GET("api/edge/trending/manga")
    suspend fun getTrendingMangaAsync(): Response<TrendingData>
}
