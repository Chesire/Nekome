package com.chesire.malime.kitsu.api.auth

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.kitsu.AuthProvider

class KitsuAuth(
    private val authService: KitsuAuthService,
    private val authProvider: AuthProvider
) : AuthApi {
    override suspend fun login(username: String, password: String): Resource<Any> {
        val response = authService.loginAsync(LoginRequest(username, password)).await()
        return if (response.isSuccessful) {
            response.body()?.let { responseObject ->
                authProvider.apply {
                    accessToken = responseObject.accessToken
                    refreshToken = responseObject.refreshToken
                }
                Resource.Success(Any())
            } ?: Resource.Error("Response body is null")
        } else {
            Resource.Error(response.errorBody()?.string() ?: response.message())
        }
    }
}
