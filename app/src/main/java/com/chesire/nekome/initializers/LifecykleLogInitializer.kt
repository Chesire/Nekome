package com.chesire.nekome.initializers

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.chesire.lifecyklelog.LifecykleLog
import com.chesire.lifecyklelog.LogHandler
import timber.log.Timber

/**
 * Initializes [LifecykleLog].
 */
class LifecykleLogInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        LifecykleLog.apply {
            initialize(context as Application)
            logHandler = LogHandler { clazz, lifecycleEvent, bundle ->
                Timber.tag(clazz)
                Timber.d("-> $lifecycleEvent ${bundle?.let { "- $it" } ?: ""}")
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        TimberInitializer::class.java
    )
}
