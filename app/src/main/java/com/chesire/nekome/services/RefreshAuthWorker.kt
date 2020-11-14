package com.chesire.nekome.services

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
import timber.log.Timber

/**
 * Worker object that handles updating a users authenticated state.
 *
 * When scheduled to run it will send a request to the [userRepo] to try to refresh the current
 * user.
 */
class RefreshAuthWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepo: UserRepository,
    private val auth: AuthApi
) : CoroutineWorker(appContext, workerParams) {

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
