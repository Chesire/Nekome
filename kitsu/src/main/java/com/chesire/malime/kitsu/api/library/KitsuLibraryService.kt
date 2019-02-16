package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.Resource
import com.chesire.malime.core.models.SeriesModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KitsuLibraryService {
    // for testing purposes might want to lower the limit to 50 or something
    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=anime" +
                "&page[limit]=500" +
                "&fields[libraryEntries]=status,progress,anime,startedAt,finishedAt" +
                "&fields[anime]=slug,canonicalTitle,status,subtype,posterImage,coverImage,episodeCount,nsfw" +
                "&filter[kind]=anime" +
                "&sort=anime.titles.canonical"
    )
    fun retrieveAnime(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int
    ): Resource<List<SeriesModel>>
}
