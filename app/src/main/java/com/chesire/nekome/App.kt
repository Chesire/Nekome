package com.chesire.nekome

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.chesire.lifecyklelog.LifecykleLog
import com.chesire.lifecyklelog.LogHandler
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.services.WorkerQueue
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

/**
 * Custom application to initialize anything that needs to be activated from application start.
 */
@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var settings: ApplicationSettings

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workerQueue: WorkerQueue

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

        setApplicationTheme()
        workerQueue.enqueueAuthRefresh()
        workerQueue.enqueueSeriesRefresh()
        workerQueue.enqueueUserRefresh()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun setApplicationTheme() = AppCompatDelegate.setDefaultNightMode(settings.theme.value)

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
