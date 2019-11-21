package com.chesire.malime.harness

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.AuthApi

open class FakeAuthApi : AuthApi {
    override suspend fun login(username: String, password: String): Resource<Any> {
        TODO("not implemented")
    }

    override suspend fun clearAuth() {
        TODO("not implemented")
    }
}
