package com.chesire.malime.injection.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.chesire.malime.core.SharedPref
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Suppress("unused")
@Module
object AppModule {
    @Provides
    @Reusable
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideSharedPref(sharedPreferences: SharedPreferences) = SharedPref(sharedPreferences)
}
