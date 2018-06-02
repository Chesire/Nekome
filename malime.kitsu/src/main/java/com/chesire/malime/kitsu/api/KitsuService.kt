package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.LibraryResponse
import com.chesire.malime.kitsu.models.LoginRequest
import com.chesire.malime.kitsu.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Provides a Retrofit service to interact with the Kitsu API.
 */
interface KitsuService {
    @POST("api/oauth/token")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

    @GET("api/edge/users?fields[users]=id")
    fun getUser(@Query("filter[name]") username: String): Call<LibraryResponse>

    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=anime,manga" +
                "&fields[libraryEntries]=status,progress,anime,manga" +
                "&fields[anime]=slug,canonicalTitle,status,posterImage,coverImage,episodeCount,nsfw" +
                "&fields[manga]=slug,canonicalTitle,status,posterImage,chapterCount"
    )
    fun getUserLibrary(
        @Path("userId") userId: Int,
        @Query("page[limit]") limit: Int,
        @Query("page[offset]") offset: Int
    ): Call<LibraryResponse>
}