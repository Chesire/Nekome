package com.chesire.malime

import android.os.StrictMode
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class MalimeApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            startStrictMode()
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
