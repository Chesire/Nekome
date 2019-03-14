package com.chesire.malime.harness

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi

open class FakeAuthApi : AuthApi {
    override suspend fun login(username: String, password: String): Resource<Any> {
        TODO("not implemented")
    }

    override suspend fun clearAuth() {
        TODO("not implemented")
    }
}
