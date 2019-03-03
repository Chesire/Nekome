package com.chesire.malime.kitsu.api.user

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

class KitsuUser @Inject constructor(private val userService: KitsuUserService) : UserApi {
    override suspend fun getUserDetails(): Resource<UserModel> {
        return try {
            userService.getUserDetailsAsync().await().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }
}
