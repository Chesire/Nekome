package com.chesire.malime.kitsu.api

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.models.UserModel

class KitsuUser(
    private val userService: KitsuUserService
) : UserApi {

    override suspend fun getUserDetails(): Resource<UserModel> {
        val callResponse = userService.getUserDetailsAsync().await()
        if (callResponse.isSuccessful) {
            callResponse.body()?.let {
                val id = it.data.first().id
                val attr = it.data.first().attributes

                val s = ""
            }
        } else {
            val error = callResponse.errorBody()
            val s = ""
        }
        return Resource.Error("Boom")
    }
}
