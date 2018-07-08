package com.chesire.malime.injection

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module(includes = [(ViewModelModule::class)])
internal class AppModule {
    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}