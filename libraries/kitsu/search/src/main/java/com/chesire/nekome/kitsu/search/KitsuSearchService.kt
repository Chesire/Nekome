package com.chesire.nekome.kitsu.search

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KitsuSearchService {
    /**
     * Performs a search for any anime series with the title [title].
     *
     * Search is limited to only 20 items at once.
     */
    @GET("api/edge/anime?fields[anime]=slug,synopsis,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,episodeCount,nsfw")
    suspend fun searchForAnimeAsync(@Query("filter[text]") title: String): Response<SearchData>

    /**
     * Performs a search for any manga series with the title [title].
     *
     * Search is limited to only 20 items at once.
     */
    @GET("api/edge/manga?fields[manga]=slug,synopsis,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,chapterCount")
    suspend fun searchForMangaAsync(@Query("filter[text]") title: String): Response<SearchData>
}
