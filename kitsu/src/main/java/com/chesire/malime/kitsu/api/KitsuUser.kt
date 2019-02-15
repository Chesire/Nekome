package com.chesire.malime.kitsu.api

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.models.UserModel

class KitsuUser(
    private val userService: KitsuUserService
) : UserApi {
    override suspend fun getUserDetails(): Resource<UserModel> {
        val callResponse = userService.getUserDetailsAsync().await()
        return if (callResponse.isSuccessful) {
            callResponse.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error("Response body is null")
        } else {
            Resource.Error(callResponse.message())
        }
    }
}
