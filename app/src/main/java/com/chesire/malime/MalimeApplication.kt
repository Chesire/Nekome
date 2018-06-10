package com.chesire.malime

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.chesire.malime.util.SharedPref
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class MalimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        context = applicationContext

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

    companion object {
        // We should be storing this as applicationContext, so should be ok
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}