package com.chesire.malime.kitsu.api.user

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.kitsu.api.ResponseParser
import javax.inject.Inject

class KitsuUser @Inject constructor(
    private val userService: KitsuUserService
) : ResponseParser,
    UserApi {

    override suspend fun getUserDetails(): Resource<UserModel> {
        val response = userService.getUserDetailsAsync().await()
        return parseResponse(response)
    }
}
