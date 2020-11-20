package com.chesire.nekome.kitsu.api.auth

import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.server.api.AuthApi
import retrofit2.Response
import javax.inject.Inject

/**
 * Provides an implementation of [AuthApi] to interact with [KitsuAuth] to log a user in.
 */
@Suppress("TooGenericExceptionCaught")
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

    private fun parseResponse(response: Response<LoginResponse>): Resource<Any> {
        return when (val parsed = response.parse()) {
            is Resource.Success -> {
                saveTokens(parsed.data)
                Resource.Success(Any())
            }
            is Resource.Error -> Resource.Error(parsed.msg, parsed.code)
        }
    }

    private fun saveTokens(loginResponse: LoginResponse) {
        authProvider.apply {
            accessToken = loginResponse.accessToken
            refreshToken = loginResponse.refreshToken
        }
    }

    override suspend fun clearAuth() = authProvider.clearAuth()
}
