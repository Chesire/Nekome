package com.chesire.malime.core.api

import com.chesire.malime.core.Resource
import com.chesire.malime.core.models.AuthModel

/**
 * Methods relating to authorizing and refreshing auth tokens.
 */
interface AuthApi {
    suspend fun login(username: String, password: String): Resource<AuthModel>
    suspend fun refreshAccessToken(refreshToken: String): Resource<AuthModel>
}
