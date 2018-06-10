package com.chesire.malime

import android.app.Application
import com.chesire.malime.util.SharedPref
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class MalimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // This is to handle switching fragments quickly while a request is occurring.
        // TODO: Revisit this, as will require some more reading into RXJava
        RxJavaPlugins.setErrorHandler {
            Timber.e(it, "Caught exception in RXJava")
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            if (SharedPref(applicationContext).getAllowCrashReporting()) {
                Fabric.with(this, Crashlytics())
            }
        }
    }
}