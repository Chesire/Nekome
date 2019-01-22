package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.request.LoginRequest
import com.chesire.malime.kitsu.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface KitsuAuthService {
    @POST("api/oauth/token")
    fun login(@Body body: LoginRequest): Call<LoginResponse>
}
