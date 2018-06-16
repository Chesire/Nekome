package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.LoginRequest
import com.chesire.malime.kitsu.models.response.AddItemResponse
import com.chesire.malime.kitsu.models.response.LibraryResponse
import com.chesire.malime.kitsu.models.response.LoginResponse
import com.chesire.malime.kitsu.models.response.UpdateItemResponse
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

    @GET("api/edge/users?fields[users]=id&filter[self]=true")
    fun getUser(): Call<LibraryResponse>

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

    // Search is limited to 20 items at once, might want to do the above if more are required
    @GET(
        "api/edge/{type}" +
                "?fields[anime]=slug,canonicalTitle,status,posterImage,coverImage,episodeCount,nsfw" +
                "&fields[manga]=slug,canonicalTitle,status,posterImage,chapterCount"
    )
    fun search(
        @Path("type") type: String,
        @Query("filter[text]") title: String
    ): Call<LibraryResponse>

    @POST(
        "api/edge/library-entries" +
                "?include=anime,manga" +
                "&fields[anime]=slug,canonicalTitle,status,posterImage,coverImage,episodeCount,nsfw" +
                "&fields[manga]=slug,canonicalTitle,status,posterImage,chapterCount"
    )
    @Headers("Content-Type: application/vnd.api+json")
    fun addItem(@Body data: RequestBody): Call<AddItemResponse>

    @PATCH("api/edge/library-entries/{id}")
    @Headers("Content-Type: application/vnd.api+json")
    fun updateItem(
        @Path("id") seriesId: Int,
        @Body data: RequestBody
    ): Call<UpdateItemResponse>
}