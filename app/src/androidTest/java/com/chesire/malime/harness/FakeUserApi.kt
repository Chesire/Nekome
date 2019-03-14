package com.chesire.malime.harness

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.models.UserModel

class FakeUserApi : UserApi {
    override suspend fun getUserDetails(): Resource<UserModel> {
        TODO("not implemented")
    }
}
