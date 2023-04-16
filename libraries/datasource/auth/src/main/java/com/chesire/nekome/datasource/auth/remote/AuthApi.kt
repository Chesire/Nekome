package com.chesire.nekome.datasource.auth.remote

import com.github.michaelbull.result.Result

/**
 * Methods relating to authorizing as a user.
 */
interface AuthApi {

    /**
     * Logs into the service using a [username] and [password], returning the success state.
     */
    suspend fun login(username: String, password: String): Result<AuthDomain, AuthFailure>

    /**
     * Refreshes any current auth credentials.
     */
    suspend fun refresh(refreshToken: String): Result<AuthDomain, AuthFailure>
}
