package com.chesire.nekome.datasource.activity.local

import com.chesire.nekome.datasource.activity.ActivityDomain
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data storage for the users activity.
 * This will cache the data in memory, as it doesn't need to have long term storage.
 */
@Singleton // Keep this file as a singleton so its a persistent cache, instead of storing it on device.
class ActivityLocalDataStorage @Inject constructor() {
    var cachedActivityItems: List<ActivityDomain> = emptyList()

    /**
     * Sets a new cache of data into the local cache.
     */
    fun setNewCache(newCache: List<ActivityDomain>) {
        cachedActivityItems = newCache
    }
}
