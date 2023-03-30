package com.chesire.nekome.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "application")

/**
 * Interact with the settings of the application, provides methods to get the user chosen values.
 * These values must be set from the settings fragment within the app-Settings module.
 */
class ApplicationPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val defaultSeriesStatePreferenceKey =
        stringPreferencesKey("preference.default-series-state")
    private val defaultHomeScreenPreferenceKey =
        stringPreferencesKey("preference.default-home-screen")
    private val themePreferenceKey =
        stringPreferencesKey("preference.theme")

    /**
     * Gets the default [UserSeriesStatus] a series should start in when a user begins tracking it.
     */
    val defaultSeriesState: Flow<UserSeriesStatus> = context.dataStore.data.map { preferences ->
        UserSeriesStatus.getFromIndex(
            preferences[defaultSeriesStatePreferenceKey]
                ?: UserSeriesStatus.Current.index.toString()
        )
    }

    suspend fun updateDefaultSeriesState(newDefaultSeriesState: UserSeriesStatus) {
        context.dataStore.edit { preferences ->
            preferences[defaultSeriesStatePreferenceKey] = newDefaultSeriesState.index.toString()
        }
    }

    /**
     * Gets the default [HomeScreenOptions] the home screen that shows after login or when re-launching.
     */
    val defaultHomeScreen: Flow<HomeScreenOptions> = context.dataStore.data.map { preferences ->
        HomeScreenOptions.getFromIndex(
            preferences[defaultHomeScreenPreferenceKey] ?: HomeScreenOptions.Anime.index.toString()
        )
    }

    suspend fun updateDefaultHomeScreen(newDefaultHomeScreen: HomeScreenOptions) {
        context.dataStore.edit { preferences ->
            preferences[defaultHomeScreenPreferenceKey] = newDefaultHomeScreen.index.toString()
        }
    }

    /**
     * Gets the [Theme] the user has chosen to use for the application.
     */
    val theme: Flow<Theme> = context.dataStore.data.map { preferences ->
        Theme.fromValue(preferences[themePreferenceKey] ?: Theme.System.value.toString())
    }

    suspend fun updateTheme(newTheme: Theme) {
        context.dataStore.edit { preferences ->
            preferences[themePreferenceKey] = newTheme.value.toString()
        }
    }
}
