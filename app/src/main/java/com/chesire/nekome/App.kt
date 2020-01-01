package com.chesire.nekome

import android.os.StrictMode
import com.chesire.lifecyklelog.LifecykleLog
import com.chesire.lifecyklelog.LogHandler
import com.chesire.nekome.injection.components.AppComponent
import com.chesire.nekome.injection.components.DaggerAppComponent
import com.chesire.nekome.services.WorkerQueue
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

/**
 * Custom application to initialize anything that needs to be activated from application start.
 */
class App : DaggerApplication() {
    lateinit var daggerComponent: AppComponent
    @Inject
    lateinit var workerQueue: WorkerQueue

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

        workerQueue.enqueueSeriesRefresh()
        workerQueue.enqueueUserRefresh()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .applicationContext(this)
            .build()
            .also { daggerComponent = it }
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
