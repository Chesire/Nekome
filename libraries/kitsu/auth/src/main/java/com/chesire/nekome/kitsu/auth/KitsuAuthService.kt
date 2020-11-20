package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.kitsu.auth.request.LoginRequest
import com.chesire.nekome.kitsu.auth.request.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface KitsuAuthService {

    @POST("api/oauth/token")
    suspend fun loginAsync(@Body body: LoginRequest): Response<KitsuAuthEntity>

    @POST("api/oauth/token")
    suspend fun refreshAccessTokenAsync(@Body body: RefreshTokenRequest): Response<KitsuAuthEntity>
}
