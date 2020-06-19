package com.chesire.nekome.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.App
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
import timber.log.Timber
import javax.inject.Inject

/**
 * Worker object that handles updating a users authenticated state.
 *
 * When scheduled to run it will send a request to the [userRepo] to try to refresh the current
 * user.
 */
class RefreshAuthWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var userRepo: UserRepository

    @Inject
    lateinit var auth: AuthApi

    init {
        // For now setup in the init block
        // dagger currently doesn't support androidInjection for workers
        Timber.i("Initializing the RefreshAuthWorker")
        if (appContext is App) {
            appContext.daggerComponent.inject(this)
        }
    }

    override suspend fun doWork(): Result {
        Timber.i("doWork RefreshAuthWorker")

        if (userRepo.retrieveUserId() == null) {
            Timber.i("doWork no userId found, so cancelling")
            return Result.success()
        }

        Timber.i("doWork userId found, beginning to refresh")
        return if (auth.refresh() is Resource.Error) {
            Result.retry()
        } else {
            Result.success()
        }
    }
}
