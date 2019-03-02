package com.chesire.malime.kitsu.api.user

import com.chesire.malime.core.api.UserApi
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

class KitsuUser @Inject constructor(private val userService: KitsuUserService) : UserApi {
    override suspend fun getUserDetails() = userService.getUserDetailsAsync().await().parse()
}
