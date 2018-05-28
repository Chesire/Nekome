package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.LoginRequest
import com.chesire.malime.kitsu.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Provides a Retrofit service to interact with the Kitsu API.
 */
interface KitsuService {
    @POST("api/oauth/token")
    fun login(@Body body: LoginRequest): Call<LoginResponse>
}