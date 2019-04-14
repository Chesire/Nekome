package com.chesire.malime.injection.modules

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
object WorkerModule {
    @Provides
    @JvmStatic
    fun providesWorkManager() = WorkManager.getInstance()
}
