package com.chesire.nekome.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.App
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.server.Resource
import timber.log.Timber
import javax.inject.Inject

/**
 * Worker object that handles updating a users Series if possible.
 *
 * When scheduled to run it will send a request to the [seriesRepo] to try to update the series,
 * letting the [seriesRepo] handle what to do with the results.
 */
class RefreshSeriesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var seriesRepo: SeriesRepository

    @Inject
    lateinit var userRepo: UserRepository

    init {
        // For now setup in the init block
        // dagger currently doesn't support androidInjection for workers
        Timber.i("Initializing the RefreshSeriesWorker")
        if (appContext is App) {
            appContext.daggerComponent.inject(this)
        }
    }

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
