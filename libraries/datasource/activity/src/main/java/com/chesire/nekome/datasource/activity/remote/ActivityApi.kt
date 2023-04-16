package com.chesire.nekome.datasource.activity.remote

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.activity.ActivityDomain
import com.github.michaelbull.result.Result

/**
 * Methods relating to retrieving the users activity.
 */
interface ActivityApi {

    /**
     * Retrieve the users latest activity.
     */
    suspend fun retrieveActivity(): Result<List<ActivityDomain>, ErrorDomain>
}
