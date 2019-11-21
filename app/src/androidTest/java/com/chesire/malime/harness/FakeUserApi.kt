package com.chesire.malime.harness

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.UserApi
import com.chesire.malime.core.models.UserModel

open class FakeUserApi : UserApi {
    override suspend fun getUserDetails(): Resource<UserModel> {
        TODO("not implemented")
    }
}
