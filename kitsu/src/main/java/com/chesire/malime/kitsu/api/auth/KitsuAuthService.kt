package com.chesire.malime.kitsu.api.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface KitsuAuthService {
    @POST("api/oauth/token")
    suspend fun loginAsync(@Body body: LoginRequest): Response<LoginResponse>

    @POST("api/oauth/token")
    suspend fun refreshAccessTokenAsync(@Body body: RefreshTokenRequest): Response<LoginResponse>
}
