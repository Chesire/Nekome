package com.chesire.malime.kitsu.api.library

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KitsuLibraryService {
    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=anime" +
                "&fields[libraryEntries]=status,progress,anime,startedAt,finishedAt" +
                "&fields[anime]=slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,episodeCount,nsfw" +
                "&filter[kind]=anime" +
                "&sort=anime.titles.canonical"
    )
    fun retrieveAnimeAsync(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Deferred<Response<ParsedLibraryResponse>>

    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=manga" +
                "&fields[libraryEntries]=status,progress,manga,startedAt,finishedAt" +
                "&fields[manga]=slug,canonicalTitle,startDate,endDate,subtype,status,posterImage,coverImage,chapterCount" +
                "&filter[kind]=manga" +
                "&sort=manga.titles.canonical"
    )
    fun retrieveMangaAsync(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int
    ): Deferred<Response<ParsedLibraryResponse>>
}
