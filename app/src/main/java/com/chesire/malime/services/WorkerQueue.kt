package com.chesire.malime.services

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val SERIES_REFRESH_TAG = "SeriesRefresh"
private const val SERIES_UNIQUE_NAME = "SeriesSync"

/**
 * Allows starting up workers.
 */
class WorkerQueue @Inject constructor(private val workManager: WorkManager) {
    /**
     * Starts up the worker to perform series refreshing.
     */
    fun enqueueSeriesRefresh() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
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
     * Cancels any queued workers.
     */
    fun cancelQueued() {
        workManager.cancelUniqueWork(SERIES_UNIQUE_NAME)
    }
}
