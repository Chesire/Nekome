package com.chesire.nekome.injection.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.chesire.nekome.app.series.SeriesPreferences
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Dagger [Module] for generic application items.
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    /**
     * Provides the default [SharedPreferences] for the application.
     */
    @Provides
    @Reusable
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Provides the [SeriesPreferences] wrapper around [SharedPreferences].
     */
    @Provides
    fun provideSharedPref(sharedPreferences: SharedPreferences) = SeriesPreferences(sharedPreferences)
}
