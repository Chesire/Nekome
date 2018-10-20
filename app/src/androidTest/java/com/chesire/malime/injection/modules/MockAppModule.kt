package com.chesire.malime.injection.modules

import android.app.Application
import android.content.Context
import com.chesire.malime.OpenForTesting
import dagger.Module
import dagger.Provides

@Suppress("unused")
@OpenForTesting
@Module
class MockAppModule {
    @Provides
    fun provideApplicationContext(app: Application): Context = app.applicationContext
}