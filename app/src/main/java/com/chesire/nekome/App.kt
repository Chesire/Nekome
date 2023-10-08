package com.chesire.nekome

import android.app.Application
import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.chesire.lifecyklelog.LifecykleLog
import com.chesire.lifecyklelog.LogHandler
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.services.DataRefreshNotifier
import com.chesire.nekome.services.WorkerQueue
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Custom application to initialize anything that needs to be activated from application start.
 */
@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var settings: ApplicationPreferences

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workerQueue: WorkerQueue

    @Inject
    lateinit var dataRefreshNotifier: DataRefreshNotifier

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            LifecykleLog.apply {
                initialize(this@App)
                requireAnnotation = false
                logHandler = LogHandler { clazz, lifecycleEvent, bundle ->
                    Timber.tag(clazz)
                    Timber.d("-> $lifecycleEvent ${bundle?.let { "- $it" } ?: ""}")
                }
            }
            startStrictMode()
        }

        workerQueue.enqueueAuthRefresh()
        workerQueue.enqueueSeriesRefresh()
        workerQueue.enqueueUserRefresh()
        GlobalScope.launch {
            dataRefreshNotifier.initialize()
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun startStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectCustomSlowCalls()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .detectFileUriExposure()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .build()
        )
    }
}
