package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.LibraryResponse
import com.chesire.malime.kitsu.models.LoginRequest
import com.chesire.malime.kitsu.models.LoginResponse
import com.chesire.malime.kitsu.models.UpdateItemResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
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
                "&fields[libraryEntries]=status,progress,anime,manga,startedAt,finishedAt" +
                "&fields[anime]=slug,canonicalTitle,status,posterImage,coverImage,episodeCount,nsfw" +
                "&fields[manga]=slug,canonicalTitle,status,posterImage,chapterCount"
    )
    fun getUserLibrary(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int = 500 // might want to reduce to say 100
    ): Call<LibraryResponse>

    @PATCH("api/edge/library-entries/{id}")
    @Headers("Content-Type: application/vnd.api+json")
    fun updateItem(
        @Path("id") seriesId: Int,
        @Body data: RequestBody
    ): Call<UpdateItemResponse>
}