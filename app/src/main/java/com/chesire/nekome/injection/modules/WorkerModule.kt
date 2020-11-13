package com.chesire.nekome.injection.modules

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Dagger [Module] to provide the systems [WorkManager].
 */
@Module
@InstallIn(ApplicationComponent::class)
object WorkerModule {
    /**
     * Provides a [WorkManager] instance to the dependency graph.
     */
    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}
