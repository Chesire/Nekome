package com.chesire.malime.kitsu.api

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.kitsu.models.request.LoginRequest
import com.chesire.malime.kitsu.models.request.RefreshTokenRequest
import com.chesire.malime.kitsu.models.response.LoginResponse
import retrofit2.Response

class KitsuAuth(
    private val authService: KitsuAuthService
) : AuthApi {

    override suspend fun login(username: String, password: String): Resource<AuthModel> {
        val callResponse = authService.loginAsync(LoginRequest(username, password))
        return handleResponse(callResponse.await())
    }

    override suspend fun refreshAccessToken(refreshToken: String): Resource<AuthModel> {
        val callResponse = authService.refreshAccessTokenAsync(RefreshTokenRequest(refreshToken))
        return handleResponse(callResponse.await())
    }

    private fun handleResponse(response: Response<LoginResponse>): Resource<AuthModel> {
        return if (response.isSuccessful) {
            response.body()?.let { responseObject ->
                Resource.Success(
                    AuthModel(
                        responseObject.accessToken,
                        responseObject.refreshToken,
                        responseObject.createdAt + responseObject.expiresIn
                    )
                )
            } ?: Resource.Error("Response body is null")
        } else {
            Resource.Error(response.message())
        }
    }
}
