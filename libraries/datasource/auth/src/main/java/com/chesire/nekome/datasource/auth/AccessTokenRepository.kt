package com.chesire.nekome.datasource.auth

import com.chesire.nekome.datasource.auth.local.AuthProvider
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthResult
import javax.inject.Inject

/**
 * Repository to access and update the users access token.
 */
class AccessTokenRepository @Inject constructor(
    private val authProvider: AuthProvider,
    private val authApi: AuthApi
) {
    /**
     * Accesses the users current access token.
     */
    val accessToken: String get() = authProvider.accessToken

    /**
     * Performs an async request to log the user in, generating access and refresh tokens.
     * On success the access and refresh tokens are stored locally.
     */
    suspend fun login(username: String, password: String): AccessTokenResult {
        val response = authApi.login(username, password)
        if (response is AuthResult.Success) {
            storeTokens(response.accessToken, response.refreshToken)
        }
        return handleResponse(response)
    }

    /**
     * Performs an async request to refresh the access tokens.
     * On success the access and refresh tokens are stored locally.
     */
    suspend fun refresh(): AccessTokenResult {
        val response = authApi.refresh(authProvider.refreshToken)
        if (response is AuthResult.Success) {
            storeTokens(response.accessToken, response.refreshToken)
        }
        return handleResponse(response)
    }

    /**
     * Clears out any locally stored tokens.
     */
    fun clear() = authProvider.clearAuth()

    private fun handleResponse(response: AuthResult): AccessTokenResult {
        return when (response) {
            is AuthResult.Success -> AccessTokenResult.Success
            AuthResult.InvalidCredentials -> AccessTokenResult.InvalidCredentials
            else -> AccessTokenResult.CommunicationError
        }
    }

    private fun storeTokens(accessToken: String, refreshToken: String) {
        authProvider.accessToken = accessToken
        authProvider.refreshToken = refreshToken
    }
}

sealed class AccessTokenResult {
    object Success : AccessTokenResult()
    object InvalidCredentials : AccessTokenResult()
    object CommunicationError : AccessTokenResult()
}
