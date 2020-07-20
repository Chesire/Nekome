package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.core.models.SeriesModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val ANIME_FIELDS =
    "slug,synopsis,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,episodeCount,nsfw"
private const val MANGA_FIELDS =
    "slug,synopsis,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,chapterCount"

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related to users library.
 */
interface KitsuLibraryService {
    /**
     * Retrieves all of the users anime series in their library.
     */
    @GET(
        "api/edge/users/{userId}/library-entries" +
            "?include=anime" +
            "&fields[libraryEntries]=status,progress,anime,startedAt,finishedAt" +
            "&fields[anime]=$ANIME_FIELDS" +
            "&filter[kind]=anime" +
            "&sort=anime.titles.canonical"
    )
    suspend fun retrieveAnimeAsync(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Response<ParsedRetrieveResponse>

    /**
     * Retrieves all of the users manga series in their library.
     */
    @GET(
        "api/edge/users/{userId}/library-entries" +
            "?include=manga" +
            "&fields[libraryEntries]=status,progress,manga,startedAt,finishedAt" +
            "&fields[manga]=$MANGA_FIELDS" +
            "&filter[kind]=manga" +
            "&sort=manga.titles.canonical"
    )
    suspend fun retrieveMangaAsync(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Response<ParsedRetrieveResponse>

    /**
     * Adds an anime series to the users library.
     */
    @POST(
        "api/edge/library-entries" +
            "?include=anime" +
            "&fields[anime]=$ANIME_FIELDS"
    )
    suspend fun addAnimeAsync(@Body data: RequestBody): Response<SeriesModel>

    /**
     * Adds a manga series to the users library.
     */
    @POST(
        "api/edge/library-entries" +
            "?include=manga" +
            "&fields[manga]=$MANGA_FIELDS"
    )
    suspend fun addMangaAsync(@Body data: RequestBody): Response<SeriesModel>

    /**
     * Updates a series in the users library with new data.
     */
    @PATCH(
        "api/edge/library-entries/{id}" +
            "?include=anime,manga" +
            "&fields[anime]=$ANIME_FIELDS" +
            "&fields[manga]=$MANGA_FIELDS"
    )
    suspend fun updateItemAsync(
        @Path("id") userSeriesId: Int,
        @Body data: RequestBody
    ): Response<SeriesModel>

    /**
     * Deletes a series from the users library.
     *
     * This returns a [Response] of [Any] as we just need to know if the request succeeded or not.
     */
    @DELETE("api/edge/library-entries/{id}")
    suspend fun deleteItemAsync(@Path("id") userSeriesId: Int): Response<Any>
}
