package com.chesire.nekome

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.chesire.lifecyklelog.LifecykleLog
import com.chesire.lifecyklelog.LogHandler
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.services.WorkerQueue
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

        runBlocking {
            // Temp fix to force the correct day/night mode on app startup.
            settings.theme.first().apply {
                AppCompatDelegate.setDefaultNightMode(this.value)
            }
        }
        MainScope().launch {
            // This can cause a flicker from light -> dark.
            // Implement correct splash screen to hide this
            setApplicationTheme()
        }

        workerQueue.enqueueAuthRefresh()
        workerQueue.enqueueSeriesRefresh()
        workerQueue.enqueueUserRefresh()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private suspend fun setApplicationTheme() {
        settings.theme.collect { theme ->
            AppCompatDelegate.setDefaultNightMode(theme.value)
        }
    }

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
