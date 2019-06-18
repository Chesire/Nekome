package com.chesire.malime.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.malime.MalimeApplication
import com.chesire.malime.core.Resource
import com.chesire.malime.repo.UserRepository
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
