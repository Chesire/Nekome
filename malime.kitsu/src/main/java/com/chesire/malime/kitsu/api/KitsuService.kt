package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.request.LoginRequest
import com.chesire.malime.kitsu.models.request.RefreshAuthRequest
import com.chesire.malime.kitsu.models.response.AddItemResponse
import com.chesire.malime.kitsu.models.response.LibraryResponse
import com.chesire.malime.kitsu.models.response.LoginResponse
import com.chesire.malime.kitsu.models.response.UpdateItemResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @POST("api/oauth/token")
    fun refreshAuth(@Body body: RefreshAuthRequest): Call<LoginResponse>

    /**
     * Gets the user ID, requires authentication to be set.
     */
    @GET("api/edge/users?fields[users]=id&filter[self]=true")
    fun getUser(): Call<LibraryResponse>

    @Deprecated("Use the getUserAnime and getUserManga")
    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=anime,manga" +
                "&fields[libraryEntries]=status,progress,anime,manga,startedAt,finishedAt" +
                "&fields[anime]=slug,canonicalTitle,status,subtype,posterImage,coverImage,episodeCount,nsfw" +
                "&fields[manga]=slug,canonicalTitle,status,subtype,posterImage,chapterCount" +
                "&filter[kind]=manga" +
                "&sort=manga.titles.canonical"
    )
    fun getUserLibrary(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int = 200
    ): Call<LibraryResponse>

    // if this limit is changed, then the kitsuManager#getUserEntriesForType offset should be changed to match
    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=anime" +
                "&fields[libraryEntries]=status,progress,anime,startedAt,finishedAt" +
                "&fields[anime]=slug,canonicalTitle,status,subtype,posterImage,coverImage,episodeCount,nsfw" +
                "&filter[kind]=anime" +
                "&sort=anime.titles.canonical"
    )
    fun getUserAnime(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int = 200
    ): Call<LibraryResponse>

    // if this limit is changed, then the kitsuManager#getUserEntriesForType offset should be changed to match
    @GET(
        "api/edge/users/{userId}/library-entries" +
                "?include=manga" +
                "&fields[libraryEntries]=status,progress,manga,startedAt,finishedAt" +
                "&fields[manga]=slug,canonicalTitle,status,subtype,posterImage,chapterCount" +
                "&filter[kind]=manga" +
                "&sort=manga.titles.canonical"
    )
    fun getUserManga(
        @Path("userId") userId: Int,
        @Query("page[offset]") offset: Int,
        @Query("page[limit]") limit: Int = 200
    ): Call<LibraryResponse>

    // Search is limited to 20 items at once, might want to do the above if more are required
    @GET(
        "api/edge/{type}" +
                "?fields[anime]=slug,canonicalTitle,status,subtype,posterImage,coverImage,episodeCount,nsfw" +
                "&fields[manga]=slug,canonicalTitle,status,subtype,posterImage,chapterCount"
    )
    fun search(
        @Path("type") type: String,
        @Query("filter[text]") title: String
    ): Call<LibraryResponse>

    @POST(
        "api/edge/library-entries" +
                "?include=anime,manga" +
                "&fields[anime]=slug,canonicalTitle,status,subtype,posterImage,coverImage,episodeCount,nsfw" +
                "&fields[manga]=slug,canonicalTitle,status,subtype,posterImage,chapterCount"
    )
    @Headers("Content-Type: application/vnd.api+json")
    fun addItem(@Body data: RequestBody): Call<AddItemResponse>

    @DELETE("api/edge/library-entries/{id}")
    fun deleteItem(@Path("id") seriesId: Int): Call<Any>

    @PATCH("api/edge/library-entries/{id}")
    @Headers("Content-Type: application/vnd.api+json")
    fun updateItem(
        @Path("id") seriesId: Int,
        @Body data: RequestBody
    ): Call<UpdateItemResponse>
}