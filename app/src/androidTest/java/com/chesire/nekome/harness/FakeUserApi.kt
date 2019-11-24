package com.chesire.nekome.harness

import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.UserApi
import com.chesire.nekome.core.models.UserModel

open class FakeUserApi : UserApi {
    override suspend fun getUserDetails(): Resource<UserModel> {
        TODO("not implemented")
    }
}
