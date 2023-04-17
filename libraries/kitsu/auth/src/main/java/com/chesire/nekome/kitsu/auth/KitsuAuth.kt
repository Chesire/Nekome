package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.datasource.auth.AuthException
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthDomain
import com.chesire.nekome.datasource.auth.remote.AuthFailure
import com.chesire.nekome.kitsu.asError
import com.chesire.nekome.kitsu.auth.dto.AuthResponseDto
import com.chesire.nekome.kitsu.auth.dto.LoginRequestDto
import com.chesire.nekome.kitsu.auth.dto.RefreshTokenRequestDto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.net.HttpURLConnection
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

    override suspend fun login(
        username: String,
        password: String
    ): Result<AuthDomain, AuthFailure> {
        return try {
            val dto = LoginRequestDto(username, password)
            parseResponse(authService.loginAsync(dto))
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    override suspend fun refresh(refreshToken: String): Result<AuthDomain, AuthFailure> {
        return try {
            val dto = RefreshTokenRequestDto(refreshToken)
            parseResponse(authService.refreshAccessTokenAsync(dto))
        } catch (ex: Exception) {
            Err(ex.parse())
        }
    }

    private fun parseResponse(response: Response<AuthResponseDto>): Result<AuthDomain, AuthFailure> {
        return if (response.isSuccessful) {
            response.body()?.let {
                Ok(
                    AuthDomain(
                        accessToken = it.accessToken,
                        refreshToken = it.refreshToken
                    )
                )
            } ?: Err(AuthFailure.BadRequest)
        } else {
            val err = response.asError()
            Timber.e("Error performing request - [${err.code}]: ${err.message}")
            if (err.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Err(AuthFailure.InvalidCredentials)
            } else {
                Err(AuthFailure.BadRequest)
            }
        }
    }

    private fun Exception.parse(): AuthFailure {
        return when (this) {
            is UnknownHostException -> AuthFailure.CouldNotReachServer
            is AuthException -> AuthFailure.CouldNotRefresh
            else -> AuthFailure.BadRequest
        }
    }
}
