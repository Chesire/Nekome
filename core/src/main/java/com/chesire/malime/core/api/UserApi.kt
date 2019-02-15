package com.chesire.malime.core.api

import com.chesire.malime.core.Resource
import com.chesire.malime.core.models.UserModel

interface UserApi {
    suspend fun getUserDetails(): Resource<UserModel>
}
