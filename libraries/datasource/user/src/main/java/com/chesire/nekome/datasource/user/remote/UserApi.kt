package com.chesire.nekome.datasource.user.remote

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.user.UserDomain
import com.github.michaelbull.result.Result

/**
 * Methods relating to getting information about a user from the api.
 */
interface UserApi {

    /**
     * Executes request to get the user details.
     */
    suspend fun getUserDetails(): Result<UserDomain, ErrorDomain>
}
