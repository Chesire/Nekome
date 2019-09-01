package com.chesire.malime.kitsu.api.user

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.UserApi
import com.chesire.malime.server.models.UserModel
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

@Suppress("TooGenericExceptionCaught")
class KitsuUser @Inject constructor(private val userService: KitsuUserService) : UserApi {
    override suspend fun getUserDetails(): Resource<UserModel> {
        return try {
            userService.getUserDetailsAsync().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }
}
