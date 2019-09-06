package com.chesire.malime.injection.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.chesire.malime.core.SharedPref
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
object AppModule {
    @Provides
    @JvmStatic
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @JvmStatic
    fun provideSharedPref(sharedPreferences: SharedPreferences) = SharedPref(sharedPreferences)
}
