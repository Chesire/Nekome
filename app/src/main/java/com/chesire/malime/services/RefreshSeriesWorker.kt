package com.chesire.malime.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.malime.MalimeApplication
import com.chesire.malime.core.Resource
import com.chesire.malime.injection.components.DaggerAppComponent
import com.chesire.malime.repo.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@Suppress("UnsafeCast")
class RefreshSeriesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override val coroutineContext = Dispatchers.IO

    @Inject
    lateinit var repo: SeriesRepository

    init {
        // For now setup in the init block
        // dagger currently doesn't support androidInjection for workers
        if (applicationContext is MalimeApplication) {
            DaggerAppComponent
                .builder()
                .applicationContext(applicationContext as MalimeApplication)
                .build()
                .inject(this)
        }
    }

    override suspend fun doWork(): Result = coroutineScope {
        if (listOf(repo.refreshAnime(), repo.refreshManga()).any { it is Resource.Error }) {
            Result.retry()
        } else {
            Result.success()
        }
    }
}
