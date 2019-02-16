package com.chesire.malime.kitsu.api.auth

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.models.AuthModel
import retrofit2.Response

class KitsuAuth(private val authService: KitsuAuthService) : AuthApi {
    override suspend fun login(username: String, password: String): Resource<AuthModel> {
        val response = authService.loginAsync(LoginRequest(username, password))
        return handleResponse(response.await())
    }

    override suspend fun refreshAccessToken(refreshToken: String): Resource<AuthModel> {
        val response = authService.refreshAccessTokenAsync(RefreshTokenRequest(refreshToken))
        return handleResponse(response.await())
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
