package com.chesire.nekome.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.server.Resource
import timber.log.Timber
import javax.inject.Inject

/**
 * Worker object that handles updating the information for a user.
 *
 * When scheduled to run it will send a request to the [userRepo] to try to refresh the current
 * user data.
 */
class RefreshUserWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var userRepo: UserRepository

    override suspend fun doWork(): Result {
        Timber.i("doWork RefreshUserWorker")

        if (userRepo.retrieveUserId() == null) {
            Timber.i("doWork no userId found, so cancelling")
            return Result.success()
        }

        Timber.i("doWork userId found, beginning to refresh")
        return if (userRepo.refreshUser() is Resource.Error) {
            Result.retry()
        } else {
            Result.success()
        }
    }
}
