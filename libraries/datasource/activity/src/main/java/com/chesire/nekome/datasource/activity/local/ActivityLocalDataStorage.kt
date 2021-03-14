package com.chesire.nekome.datasource.activity.local

import com.chesire.nekome.datasource.activity.ActivityDomain
import javax.inject.Inject
import javax.inject.Singleton

// Keep this file as a singleton so its a persistent cache, instead of storing it on device.
@Singleton
class ActivityLocalDataStorage @Inject constructor() {
    var cachedActivityItems: List<ActivityDomain> = emptyList()

    fun setNewCache(newCache: List<ActivityDomain>) {
        cachedActivityItems = newCache
    }
}
