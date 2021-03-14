package com.chesire.nekome.datasource.activity

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.activity.local.ActivityLocalDataStorage
import com.chesire.nekome.datasource.activity.remote.ActivityApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val localData: ActivityLocalDataStorage,
    private val remoteData: ActivityApi
) {

    val userActivity = flow {
        val retrievedValues = localData.cachedActivityItems
        if (retrievedValues.isNotEmpty()) {
            emit(UserActivityResult.RetrievedValues(localData.cachedActivityItems))
        }

        val newItems = remoteData.retrieveActivity()
        if (newItems is Resource.Success) {
            localData.setNewCache(newItems.data)
            emit(UserActivityResult.RetrievedValues(newItems.data))
        } else {
            emit(UserActivityResult.Failure)
        }
    }
}

sealed class UserActivityResult {
    data class RetrievedValues(val newValues: List<ActivityDomain>) : UserActivityResult()
    object Failure : UserActivityResult()
}
