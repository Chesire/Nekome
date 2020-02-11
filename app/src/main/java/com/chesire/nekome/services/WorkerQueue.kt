package com.chesire.nekome.services

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val AUTH_REFRESH_TAG = "AuthRefresh"
private const val AUTH_UNIQUE_NAME = "AuthSync"
private const val SERIES_REFRESH_TAG = "SeriesRefresh"
private const val SERIES_UNIQUE_NAME = "SeriesSync"
private const val USER_REFRESH_TAG = "UserRefresh"
private const val USER_UNIQUE_NAME = "UserSync"

/**
 * Allows starting up workers.
 */
class WorkerQueue @Inject constructor(private val workManager: WorkManager) {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    /**
     * Starts up the worker to perform auth refreshing.
     */
    fun enqueueAuthRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshAuthWorker>(7, TimeUnit.DAYS)
            .setConstraints(constraints)
            .addTag(AUTH_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            AUTH_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Starts up the worker to perform series refreshing.
     */
    fun enqueueSeriesRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshSeriesWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(SERIES_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            SERIES_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Starts up the worker to perform user refreshing.
     */
    fun enqueueUserRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshUserWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(USER_REFRESH_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            USER_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Cancels any queued workers.
     */
    fun cancelQueued() {
        workManager.cancelUniqueWork(SERIES_UNIQUE_NAME)
        workManager.cancelUniqueWork(USER_UNIQUE_NAME)
    }
}
