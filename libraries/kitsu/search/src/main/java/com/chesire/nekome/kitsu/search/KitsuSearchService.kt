package com.chesire.nekome.kitsu.search

import com.chesire.nekome.kitsu.search.dto.SearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val ANIME_FIELDS = "synopsis,titles,canonicalTitle,subtype,posterImage"
private const val MANGA_FIELDS = "synopsis,titles,canonicalTitle,subtype,posterImage"

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related searching series.
 */
interface KitsuSearchService {

    /**
     * Performs a search for any anime series with the title [title].
     *
     * Search is limited to only 20 items at once.
     */
    @GET("api/edge/anime?fields[anime]=$ANIME_FIELDS")
    suspend fun searchForAnimeAsync(
        @Query("filter[text]") title: String
    ): Response<SearchResponseDto>

    /**
     * Performs a search for any manga series with the title [title].
     *
     * Search is limited to only 20 items at once.
     */
    @GET("api/edge/manga?fields[manga]=$MANGA_FIELDS")
    suspend fun searchForMangaAsync(
        @Query("filter[text]") title: String
    ): Response<SearchResponseDto>
}
