package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.kitsu.auth.dto.AuthResponseDto
import com.chesire.nekome.kitsu.auth.dto.LoginRequestDto
import com.chesire.nekome.kitsu.auth.dto.RefreshTokenRequestDto
import com.chesire.nekome.kitsu.parse
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of the [AuthApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuAuth @Inject constructor(
    private val authService: KitsuAuthService,
    private val authProvider: AuthProvider
) : AuthApi {

    override suspend fun login(username: String, password: String): Resource<Any> {
        return try {
            parseResponse(authService.loginAsync(LoginRequestDto(username, password)))
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun refresh(): Resource<Any> {
        return try {
            parseResponse(
                authService.refreshAccessTokenAsync(
                    RefreshTokenRequestDto(authProvider.refreshToken)
                )
            )
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun clearAuth() = authProvider.clearAuth()

    private fun parseResponse(response: Response<AuthResponseDto>): Resource<Any> {
        return when (val parsed = response.parse()) {
            is Resource.Success -> {
                saveTokens(parsed.data)
                Resource.Success(Any())
            }
            is Resource.Error -> Resource.Error(parsed.msg, parsed.code)
        }
    }

    private fun saveTokens(dto: AuthResponseDto) {
        authProvider.apply {
            accessToken = dto.accessToken
            refreshToken = dto.refreshToken
        }
    }
}
