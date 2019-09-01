package com.chesire.malime.server.api

import com.chesire.malime.core.models.UserModel
import com.chesire.malime.server.Resource

interface UserApi {
    suspend fun getUserDetails(): Resource<UserModel>
}
