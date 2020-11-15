package com.chesire.nekome.server.api

import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.core.Resource

/**
 * Methods relating to a users profile.
 */
interface UserApi {
    /**
     * Get all of the user details.
     */
    suspend fun getUserDetails(): Resource<UserModel>
}
