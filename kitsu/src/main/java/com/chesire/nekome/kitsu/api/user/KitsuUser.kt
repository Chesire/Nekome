package com.chesire.nekome.kitsu.api.user

import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.UserApi
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.kitsu.parse
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
