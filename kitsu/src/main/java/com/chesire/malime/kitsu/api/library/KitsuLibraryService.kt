package com.chesire.malime.kitsu.api.library

import com.chesire.malime.server.models.SeriesModel
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
    "slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,episodeCount,nsfw"
private const val MANGA_FIELDS =
    "slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,chapterCount"

interface KitsuLibraryService {
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

    @POST(
        "api/edge/library-entries" +
                "?include=anime" +
                "&fields[anime]=$ANIME_FIELDS"
    )
    suspend fun addAnimeAsync(@Body data: RequestBody): Response<SeriesModel>

    @POST(
        "api/edge/library-entries" +
                "?include=manga" +
                "&fields[manga]=$MANGA_FIELDS"
    )
    suspend fun addMangaAsync(@Body data: RequestBody): Response<SeriesModel>

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

    @DELETE("api/edge/library-entries/{id}")
    suspend fun deleteItemAsync(@Path("id") userSeriesId: Int): Response<Any>
}
