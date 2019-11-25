package com.chesire.nekome.injection.modules

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

/**
 * Dagger [Module] to provide the systems [WorkManager].
 */
@Suppress("unused")
@Module
object WorkerModule {
    /**
     * Provides a [WorkManager] instance to the dependency graph.
     */
    @Provides
    fun providesWorkManager(context: Context) = WorkManager.getInstance(context)
}
