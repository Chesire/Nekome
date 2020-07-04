package com.chesire.nekome.initializers

import android.content.Context
import androidx.startup.Initializer
import com.chesire.nekome.BuildConfig
import timber.log.Timber

/**
 * Initializes the [Timber] logger, logging to the debug tree.
 */
class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
