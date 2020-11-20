package com.chesire.nekome.auth.api

import com.chesire.nekome.core.Resource

interface AuthApi {

    suspend fun login(username: String, password: String): Resource<Any>

    suspend fun refresh(): Resource<Any>

    suspend fun clearAuth()
}
