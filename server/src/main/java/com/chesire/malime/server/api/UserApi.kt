package com.chesire.malime.server.api

import com.chesire.malime.core.models.UserModel
import com.chesire.malime.server.Resource

/**
 * Methods relating to a users profile.
 */
interface UserApi {
    /**
     * Get all of the user details.
     */
    suspend fun getUserDetails(): Resource<UserModel>
}
