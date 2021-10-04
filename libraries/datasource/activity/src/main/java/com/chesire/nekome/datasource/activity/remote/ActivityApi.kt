package com.chesire.nekome.datasource.activity.remote

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.activity.ActivityDomain

/**
 * Methods relating to retrieving the users activity.
 */
interface ActivityApi {

    /**
     * Retrieve the users latest activity.
     */
    suspend fun retrieveActivity(): Resource<List<ActivityDomain>>
}
