package com.chesire.nekome.auth.api

import com.chesire.nekome.core.Resource

interface AuthApi {

    suspend fun loginAsync(username: String, password: String): Resource<Any>

    suspend fun refreshAsync(): Resource<Any>

    suspend fun clearAuthAsync()
}
