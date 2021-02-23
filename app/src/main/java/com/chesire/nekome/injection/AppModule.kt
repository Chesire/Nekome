package com.chesire.nekome.injection

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Dagger [Module] for generic application items.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides the default [SharedPreferences] for the application.
     */
    @Provides
    @Reusable
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}
