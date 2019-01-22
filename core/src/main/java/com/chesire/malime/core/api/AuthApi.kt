package com.chesire.malime.core.api

/**
 * Methods relating to authorizing and refreshing auth tokens.
 */
interface AuthApi {
    fun login(username: String, password: String)
    fun refreshToken()
}
