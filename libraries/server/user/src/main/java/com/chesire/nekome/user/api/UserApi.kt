package com.chesire.nekome.user.api

import com.chesire.nekome.core.Resource

/**
 * Methods relating to getting information about a user.
 */
interface UserApi {
    /**
     * Retrieves the users details.
     */
    suspend fun getUserDetails(): Resource<UserEntity>
}
