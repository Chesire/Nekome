package com.chesire.malime.injection

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AppModule {
    @Singleton
    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}