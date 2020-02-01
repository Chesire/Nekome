package com.chesire.nekome.injection.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.chesire.nekome.app.series.SharedPref
import dagger.Module
import dagger.Provides
import dagger.Reusable

/**
 * Dagger [Module] for generic application items.
 */
@Suppress("unused")
@Module
object AppModule {
    /**
     * Provides the default [SharedPreferences] for the application.
     */
    @Provides
    @Reusable
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Provides the [SharedPref] wrapper around [SharedPreferences].
     */
    @Provides
    fun provideSharedPref(sharedPreferences: SharedPreferences) = SharedPref(sharedPreferences)
}
