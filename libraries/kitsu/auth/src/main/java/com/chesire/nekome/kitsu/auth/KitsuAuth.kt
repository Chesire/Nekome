package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.auth.AuthException
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthResult
import com.chesire.nekome.kitsu.auth.dto.AuthResponseDto
import com.chesire.nekome.kitsu.auth.dto.LoginRequestDto
import com.chesire.nekome.kitsu.auth.dto.RefreshTokenRequestDto
import com.chesire.nekome.kitsu.parse
import java.net.UnknownHostException
import javax.inject.Inject
import retrofit2.Response
import timber.log.Timber

/**
 * Implementation of the [AuthApi] for usage with the Kitsu API.
 */
@Suppress("TooGenericExceptionCaught")
class KitsuAuth @Inject constructor(
    private val authService: KitsuAuthService
) : AuthApi {

    override suspend fun login(username: String, password: String): AuthResult {
        return try {
            val dto = LoginRequestDto(username, password)
            parseResponse(authService.loginAsync(dto))
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun refresh(refreshToken: String): AuthResult {
        return try {
            val dto = RefreshTokenRequestDto(refreshToken)
            parseResponse(authService.refreshAccessTokenAsync(dto))
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    private fun parseResponse(response: Response<AuthResponseDto>): AuthResult {
        return when (val parsed = response.parse()) {
            is Resource.Success -> AuthResult.Success(
                parsed.data.accessToken,
                parsed.data.refreshToken
            )
            is Resource.Error -> {
                Timber.e("Error performing request - [${parsed.code}]: ${parsed.msg}")
                if (parsed.code == Resource.Error.InvalidCredentials) {
                    AuthResult.InvalidCredentials
                } else {
                    AuthResult.BadRequest
                }
            }
        }
    }

    private fun Exception.parse(): AuthResult {
        return when (this) {
            is UnknownHostException -> AuthResult.CouldNotReachServer
            is AuthException -> AuthResult.CouldNotRefresh
            else -> AuthResult.BadRequest
        }
    }
}
