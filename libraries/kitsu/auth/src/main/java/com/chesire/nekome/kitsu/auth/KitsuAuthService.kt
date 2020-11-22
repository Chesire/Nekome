package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.kitsu.auth.request.LoginRequest
import com.chesire.nekome.kitsu.auth.request.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Constructed using Retrofit to interface with the Kitsu API for queries related to authenticating.
 */
interface KitsuAuthService {

    /**
     * Performs a login request with the API.
     */
    @POST("api/oauth/token")
    suspend fun loginAsync(@Body body: LoginRequest): Response<KitsuAuthEntity>

    /**
     * Performs a refresh token request with the API.
     */
    @POST("api/oauth/token")
    suspend fun refreshAccessTokenAsync(@Body body: RefreshTokenRequest): Response<KitsuAuthEntity>
}
