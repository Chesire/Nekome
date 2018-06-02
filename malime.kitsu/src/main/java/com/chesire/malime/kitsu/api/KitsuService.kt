package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.FilterResponse
import com.chesire.malime.kitsu.models.LoginRequest
import com.chesire.malime.kitsu.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Provides a Retrofit service to interact with the Kitsu API.
 */
interface KitsuService {
    @POST("api/oauth/token")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

    @GET("api/edge/users?fields[users]=id")
    fun getUser(@Query("filter[name]") username: String): Call<FilterResponse>
}