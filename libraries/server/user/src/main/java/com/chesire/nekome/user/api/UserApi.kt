package com.chesire.nekome.user.api

import com.chesire.nekome.core.Resource

interface UserApi {
    suspend fun getUserDetailsAsync(): Resource<UserEntity>
}
