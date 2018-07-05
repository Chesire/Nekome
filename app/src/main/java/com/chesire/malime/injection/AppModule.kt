package com.chesire.malime.injection

import android.app.Application
import com.chesire.malime.util.SharedPref
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AppModule {
    @Singleton
    @Provides
    fun provideSharedPref(application: Application): SharedPref {
        return SharedPref(application.applicationContext)
    }
}