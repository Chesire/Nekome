package com.chesire.malime

import android.app.Application
import timber.log.Timber

class MalimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}