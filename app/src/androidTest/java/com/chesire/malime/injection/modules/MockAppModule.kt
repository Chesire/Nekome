package com.chesire.malime.injection.modules

import android.app.Application
import android.content.Context
import com.chesire.malime.OpenForTesting
import com.chesire.malime.util.SharedPref
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@OpenForTesting
@Module
class MockAppModule {
    @Provides
    fun provideApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPref(context: Context): SharedPref = SharedPref(context)
}