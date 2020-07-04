package com.chesire.nekome

import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.injection.components.AppComponent
import com.chesire.nekome.injection.components.DaggerAppComponent
import com.chesire.nekome.services.WorkerQueue
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

/**
 * Custom application to initialize anything that needs to be activated from application start.
 */
class App : DaggerApplication() {
    lateinit var daggerComponent: AppComponent

    @Inject
    lateinit var workerQueue: WorkerQueue

    @Inject
    lateinit var settings: ApplicationSettings

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            startStrictMode()
        }

        setApplicationTheme()
        workerQueue.enqueueAuthRefresh()
        workerQueue.enqueueSeriesRefresh()
        workerQueue.enqueueUserRefresh()
    }

    private fun setApplicationTheme() = AppCompatDelegate.setDefaultNightMode(settings.theme.value)

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
