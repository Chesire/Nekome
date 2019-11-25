package com.chesire.nekome.kitsu.api.search

import com.chesire.nekome.core.models.SeriesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related to search.
 */
@Suppress("MaxLineLength")
interface KitsuSearchService {
    /**
     * Performs a search for any anime series with the title [title].
     *
     * Search is limited to only 20 items at once.
     */
    @GET("api/edge/anime?fields[anime]=slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,episodeCount,nsfw")
    suspend fun searchForAnimeAsync(@Query("filter[text]") title: String): Response<List<SeriesModel>>

    /**
     * Performs a search for any manga series with the title [title].
     *
     * Search is limited to only 20 items at once.
     */
    @GET("api/edge/manga?fields[manga]=slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,chapterCount")
    suspend fun searchForMangaAsync(@Query("filter[text]") title: String): Response<List<SeriesModel>>
}
