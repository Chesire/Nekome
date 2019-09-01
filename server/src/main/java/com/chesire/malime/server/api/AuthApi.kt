package com.chesire.malime.server.api

import com.chesire.malime.server.Resource

/**
 * Methods relating to authorizing and refreshing auth tokens.
 */
interface AuthApi {
    suspend fun login(username: String, password: String): Resource<Any>
    suspend fun clearAuth()
}
