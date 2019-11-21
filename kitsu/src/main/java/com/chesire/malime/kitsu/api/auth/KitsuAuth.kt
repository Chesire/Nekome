package com.chesire.malime.kitsu.api.auth

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.AuthApi
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

@Suppress("TooGenericExceptionCaught")
class KitsuAuth @Inject constructor(
    private val authService: KitsuAuthService,
    private val authProvider: AuthProvider
) : AuthApi {
    override suspend fun login(username: String, password: String): Resource<Any> {
        return try {
            when (val response = authService.loginAsync(LoginRequest(username, password)).parse()) {
                is Resource.Success -> {
                    authProvider.apply {
                        accessToken = response.data.accessToken
                        refreshToken = response.data.refreshToken
                    }
                    Resource.Success(Any())
                }
                is Resource.Error -> Resource.Error(response.msg, response.code)
            }
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun clearAuth() = authProvider.clearAuth()
}
