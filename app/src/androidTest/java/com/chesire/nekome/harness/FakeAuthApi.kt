package com.chesire.nekome.harness

import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi

open class FakeAuthApi : AuthApi {
    override suspend fun login(username: String, password: String): Resource<Any> {
        TODO("not implemented")
    }

    override suspend fun clearAuth() {
        TODO("not implemented")
    }
}
