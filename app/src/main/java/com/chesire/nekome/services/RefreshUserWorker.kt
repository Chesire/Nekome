package com.chesire.nekome.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.MalimeApplication
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.server.Resource
import timber.log.Timber
import javax.inject.Inject

class RefreshUserWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var userRepo: UserRepository

    init {
        // For now setup in the init block
        // dagger currently doesn't support androidInjection for workers
        Timber.i("Initializing the RefreshUserWorker")
        if (appContext is MalimeApplication) {
            appContext.daggerComponent.inject(this)
        }
    }

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
