package com.chesire.nekome.datasource.auth.remote

/**
 * Methods relating to authorizing as a user.
 */
interface AuthApi {

    /**
     * Logs into the service using a [username] and [password], returning the success state.
     */
    suspend fun login(username: String, password: String): AuthResult

    /**
     * Refreshes any current auth credentials.
     */
    suspend fun refresh(refreshToken: String): AuthResult
}
