package com.chesire.nekome.datasource.activity

import com.chesire.nekome.datasource.activity.local.ActivityLocalDataStorage
import com.chesire.nekome.datasource.activity.remote.ActivityApi
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

/**
 * Repository to access the users library activity.
 */
class ActivityRepository @Inject constructor(
    private val localData: ActivityLocalDataStorage,
    private val remoteData: ActivityApi
) {

    val userActivity = flow {
        val retrievedValues = localData.cachedActivityItems
        if (retrievedValues.isNotEmpty()) {
            emit(UserActivityResult.RetrievedValues(localData.cachedActivityItems))
        }

        remoteData.retrieveActivity()
            .onSuccess {
                localData.setNewCache(it)
                emit(UserActivityResult.RetrievedValues(it))
            }
            .onFailure {
                emit(UserActivityResult.Failure)
            }
    }
}

/**
 * Result class for getting a users [ActivityDomain].
 */
sealed class UserActivityResult {

    /**
     * Success result with the users activity data.
     */
    data class RetrievedValues(val newValues: List<ActivityDomain>) : UserActivityResult()

    /**
     * Failure result for retrieving users activity data.
     */
    object Failure : UserActivityResult()
}
