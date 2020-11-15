package com.chesire.nekome.services

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.core.Resource
import timber.log.Timber

/**
 * Worker object that handles updating a users Series if possible.
 *
 * When scheduled to run it will send a request to the [seriesRepo] to try to update the series,
 * letting the [seriesRepo] handle what to do with the results.
 */
class RefreshSeriesWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val seriesRepo: SeriesRepository,
    private val userRepo: UserRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Timber.i("doWork RefreshSeriesWorker")

        if (userRepo.retrieveUserId() == null) {
            Timber.i("doWork no userId found, so cancelling")
            return Result.success()
        }

        Timber.i("doWork userId found, beginning to refresh")
        return if (
            listOf(
                seriesRepo.refreshAnime(),
                seriesRepo.refreshManga()
            ).any { it is Resource.Error }
        ) {
            Result.retry()
        } else {
            Result.success()
        }
    }
}
