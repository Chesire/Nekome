package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.kitsu.auth.request.LoginRequest
import com.chesire.nekome.kitsu.auth.request.RefreshTokenRequest
import com.chesire.nekome.kitsu.parse
import retrofit2.Response
import javax.inject.Inject

class KitsuAuth @Inject constructor(
    private val authService: KitsuAuthService,
    private val authProvider: AuthProvider
) : AuthApi {

    override suspend fun login(username: String, password: String): Resource<Any> {
        return try {
            parseResponse(authService.loginAsync(LoginRequest(username, password)))
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun refresh(): Resource<Any> {
        return try {
            parseResponse(
                authService.refreshAccessTokenAsync(
                    RefreshTokenRequest(authProvider.refreshToken)
                )
            )
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun clearAuth() = authProvider.clearAuth()

    private fun parseResponse(response: Response<KitsuAuthEntity>): Resource<Any> {
        return when (val parsed = response.parse()) {
            is Resource.Success -> {
                saveTokens(parsed.data)
                Resource.Success(Any())
            }
            is Resource.Error -> Resource.Error(parsed.msg, parsed.code)
        }
    }

    private fun saveTokens(entity: KitsuAuthEntity) {
        authProvider.apply {
            accessToken = entity.accessToken
            refreshToken = entity.refreshToken
        }
    }
}
