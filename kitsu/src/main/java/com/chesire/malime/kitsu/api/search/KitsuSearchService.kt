package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.models.SeriesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@Suppress("MaxLineLength")
interface KitsuSearchService {
    // Search is limited to 20 items at once
    @GET("api/edge/anime?fields[anime]=slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,episodeCount,nsfw")
    suspend fun searchForAnimeAsync(@Query("filter[text]") title: String): Response<List<SeriesModel>>

    @GET("api/edge/manga?fields[manga]=slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,chapterCount")
    suspend fun searchForMangaAsync(@Query("filter[text]") title: String): Response<List<SeriesModel>>
}
