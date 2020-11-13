package com.chesire.nekome

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.chesire.lifecyklelog.LifecykleLog
import com.chesire.lifecyklelog.LogHandler
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.services.WorkerQueue
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Custom application to initialize anything that needs to be activated from application start.
 */
@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var workerQueue: WorkerQueue

    @Inject
    lateinit var settings: ApplicationSettings

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            LifecykleLog.apply {
                initialize(this@App)
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
