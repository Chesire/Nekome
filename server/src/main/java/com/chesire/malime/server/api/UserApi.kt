package com.chesire.malime.server.api

import com.chesire.malime.server.Resource
import com.chesire.malime.core.models.UserModel

interface UserApi {
    suspend fun getUserDetails(): Resource<UserModel>
}
