package com.chesire.nekome.datasource.user.remote

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.user.UserDomain

/**
 * Methods relating to getting information about a user from the api.
 */
interface UserApi {

    /**
     * Executes request to get the user details.
     */
    suspend fun getUserDetails(): Resource<UserDomain>
}
