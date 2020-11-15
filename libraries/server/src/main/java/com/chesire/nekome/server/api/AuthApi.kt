package com.chesire.nekome.server.api

import com.chesire.nekome.core.Resource

/**
 * Methods relating to authorizing and refreshing auth tokens.
 */
interface AuthApi {
    /**
     * Sends a login request to the server, returning the result in a standard [Resource].
     */
    suspend fun login(username: String, password: String): Resource<Any>

    /**
     * Sends a request to refresh any currently stored tokens. The result is returned in a standard
     * [Resource].
     */
    suspend fun refresh(): Resource<Any>

    /**
     * Clears the auth details out.
     */
    suspend fun clearAuth()
}
