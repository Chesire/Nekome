package com.chesire.nekome.injection

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Dagger [Module] to provide the systems [WorkManager].
 */
@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    /**
     * Provides a [WorkManager] instance to the dependency graph.
     */
    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}
