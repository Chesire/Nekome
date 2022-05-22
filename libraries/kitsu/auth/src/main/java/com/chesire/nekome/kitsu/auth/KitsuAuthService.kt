package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.kitsu.auth.dto.AuthResponseDto
import com.chesire.nekome.kitsu.auth.dto.LoginRequestDto
import com.chesire.nekome.kitsu.auth.dto.RefreshTokenRequestDto
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
    suspend fun loginAsync(@Body body: LoginRequestDto): Response<AuthResponseDto>

    /**
     * Performs a refresh token request with the API.
     */
    @POST("api/oauth/token")
    suspend fun refreshAccessTokenAsync(
        @Body body: RefreshTokenRequestDto
    ): Response<AuthResponseDto>
}
