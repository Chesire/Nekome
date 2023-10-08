package com.chesire.nekome.services

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.feature.serieswidget.ui.SeriesWidget

class WidgetDataWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        SeriesWidget().updateAll(context)
        return Result.success()
    }
}
