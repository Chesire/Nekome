package com.chesire.malime.injection.modules

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
object WorkerModule {
    @Provides
    fun providesWorkManager(context: Context) = WorkManager.getInstance(context)
}
