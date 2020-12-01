package com.chesire.nekome.auth.api

import com.chesire.nekome.core.Resource

/**
 * Methods relating to authorizing as a user.
 */
interface AuthApi {

    /**
     * Logs into the service using a [username] and [password], returning the success state.
     */
    suspend fun login(username: String, password: String): Resource<Any>

    /**
     * Refreshes any current auth credentials.
     */
    suspend fun refresh(): Resource<Any>

    /**
     * Clears out any current auth credentials.
     */
    suspend fun clearAuth()
}
