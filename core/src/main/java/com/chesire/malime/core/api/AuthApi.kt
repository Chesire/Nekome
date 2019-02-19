package com.chesire.malime.core.api

import com.chesire.malime.core.Resource

/**
 * Methods relating to authorizing and refreshing auth tokens.
 */
interface AuthApi {
    suspend fun login(username: String, password: String): Resource<Any>
}
