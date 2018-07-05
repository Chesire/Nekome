package com.chesire.malime

import android.app.Activity
import android.app.Application
import android.os.StrictMode
import com.chesire.malime.injection.DaggerAppComponent
import com.chesire.malime.util.SharedPref
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

class MalimeApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        startLeakCanary()

        // This is to handle switching fragments quickly while a request is occurring.
        // TODO: Revisit this, as will require some more reading into RXJava
        RxJavaPlugins.setErrorHandler {
            Timber.e(it, "Caught exception in RXJava")
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            startStrictMode()
        } else {
            if (SharedPref(applicationContext).getAllowCrashReporting()) {
                Fabric.with(this, Crashlytics())
            }
        }

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    private fun startLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    private fun startStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                //.detectDiskReads()  // This should be restored once some optimisation has been run
                //.detectDiskWrites() // This should be restored once some optimisation has been run
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