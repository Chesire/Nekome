package com.chesire.nekome.services

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val WIDGET_DATA_NOTIFY_TAG = "WidgetData"
private const val WIDGET_DATA_UNIQUE_NAME = "WidgetSync"

@Singleton
class DataRefreshNotifier @Inject constructor(
    private val workManager: WorkManager,
    private val seriesRepository: SeriesRepository
) {

    /**
     * Initialize the notifier and listen to any data updates.
     */
    suspend fun initialize() {
        seriesRepository.getSeries().collect {
            val request = OneTimeWorkRequestBuilder<WidgetDataWorker>()
                .addTag(WIDGET_DATA_NOTIFY_TAG)
                .build()

            workManager.enqueueUniqueWork(
                WIDGET_DATA_UNIQUE_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }
}
