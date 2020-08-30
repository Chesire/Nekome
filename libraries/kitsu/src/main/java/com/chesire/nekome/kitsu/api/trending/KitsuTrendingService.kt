package com.chesire.nekome.kitsu.api.trending

import com.chesire.nekome.core.models.SeriesModel
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
    suspend fun getTrendingAnimeAsync(): Response<List<SeriesModel>>

    /**
     * Gets the top trending Manga from Kitsu.
     */
    @GET("api/edge/trending/manga")
    suspend fun getTrendingMangaAsync(): Response<List<SeriesModel>>
}
