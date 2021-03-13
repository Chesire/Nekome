package com.chesire.nekome.activity.api

import com.chesire.nekome.core.Resource

/**
 * Methods relating to retrieving the users activity.
 */
interface ActivityApi {

    /**
     * Retrieve the users latest activity.
     */
    suspend fun retrieveActivity(): Resource<List<ActivityDomain>>
}
