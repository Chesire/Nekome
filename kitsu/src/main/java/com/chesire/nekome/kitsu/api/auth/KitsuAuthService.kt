package com.chesire.nekome.kitsu.api.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related to authorization.
 */
interface KitsuAuthService {
    /**
     * Sends up a login request, returns a [LoginResponse] containing the access token required for
     * more operations.
     */
    @POST("api/oauth/token")
    suspend fun loginAsync(@Body body: LoginRequest): Response<LoginResponse>

    /**
     * Refreshes the access token using a refresh token.
     */
    @POST("api/oauth/token")
    suspend fun refreshAccessTokenAsync(@Body body: RefreshTokenRequest): Response<LoginResponse>
}
