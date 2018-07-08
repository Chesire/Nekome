package com.chesire.malime.injection.modules

import android.app.Application
import android.content.Context
import com.chesire.malime.injection.androidmodules.ViewModelModule
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