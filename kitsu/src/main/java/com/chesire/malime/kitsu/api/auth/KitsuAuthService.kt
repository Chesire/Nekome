package com.chesire.malime.kitsu.api.auth

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface KitsuAuthService {
    @POST("api/oauth/token")
    fun loginAsync(@Body body: LoginRequest): Deferred<Response<LoginResponse>>

    @POST("api/oauth/token")
    fun refreshAccessTokenAsync(@Body body: RefreshTokenRequest): Deferred<Response<LoginResponse>>
}
