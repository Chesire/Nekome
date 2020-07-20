package com.chesire.nekome.kitsu.api.user

import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.UserApi
import javax.inject.Inject

/**
 * Provides an implementation of [UserApi] to interact with [KitsuUser] to get the users details.
 */
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
