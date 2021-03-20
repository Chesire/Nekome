package com.chesire.nekome.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.user.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

/**
 * Worker object that handles updating a users authenticated state.
 *
 * When scheduled to run it will send a request to the [userRepo] to try to refresh the current
 * user.
 */
@HiltWorker
class RefreshAuthWorker @AssistedInject constructor(
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
