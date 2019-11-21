package com.chesire.malime.server.api

import com.chesire.malime.server.Resource

/**
 * Methods relating to authorizing and refreshing auth tokens.
 */
interface AuthApi {
    /**
     * Sends a login request to the server, returning the result in a standard [Resource].
     */
    suspend fun login(username: String, password: String): Resource<Any>

    /**
     * Clears the auth details out.
     */
    suspend fun clearAuth()
}
