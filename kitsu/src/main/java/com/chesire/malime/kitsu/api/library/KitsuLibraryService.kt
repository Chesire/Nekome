package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.models.SeriesModel
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val ANIME_FIELDS =
    "slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,episodeCount,nsfw"

interface KitsuLibraryService {
    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=anime" +
                "&fields[libraryEntries]=status,progress,anime,startedAt,finishedAt" +
                "&fields[anime]=$ANIME_FIELDS" +
                "&filter[kind]=anime" +
                "&sort=anime.titles.canonical"
    )
    fun retrieveAnimeAsync(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Deferred<Response<ParsedRetrieveResponse>>

    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=manga" +
                "&fields[libraryEntries]=status,progress,manga,startedAt,finishedAt" +
                "&fields[manga]=slug,canonicalTitle,startDate,endDate," +
                "subtype,status,posterImage,coverImage,chapterCount" +
                "&filter[kind]=manga" +
                "&sort=manga.titles.canonical"
    )
    fun retrieveMangaAsync(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Deferred<Response<ParsedRetrieveResponse>>

    @POST(
        "api/edge/library-entries" +
                "?include=anime" +
                "&fields[anime]=$ANIME_FIELDS"
    )
    fun addAnimeAsync(@Body data: RequestBody): Deferred<Response<SeriesModel>>
}
