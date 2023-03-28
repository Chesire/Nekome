package com.chesire.nekome.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
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
@Suppress("UseDataClass")
class ApplicationPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: SharedPreferences
) {

    private val _defaultSeriesState = context.getString(R.string.key_default_series_state)
    private val _defaultHomeScreen = context.getString(R.string.key_default_home)
    private val themePreferenceKey = stringPreferencesKey("preference.theme")

    /**
     * Gets the default [UserSeriesStatus] a series should start in when a user begins tracking it.
     */
    val defaultSeriesState: UserSeriesStatus
        get() {
            val index = requireNotNull(
                preferences.getString(
                    _defaultSeriesState,
                    UserSeriesStatus.Current.index.toString()
                )
            ) {
                "Preferences defaultSeriesState returned null, with supplied default value"
            }
            var status = UserSeriesStatus.getFromIndex(index)
            if (status == UserSeriesStatus.Unknown) {
                // Something has gone wrong, this shouldn't be possible.
                // Reset the state of the setting and return "Current".
                preferences.edit {
                    putString(
                        _defaultSeriesState,
                        UserSeriesStatus.Current.index.toString()
                    )
                }
                status = UserSeriesStatus.Current
            }

            return status
        }

    /**
     * Gets the default [HomeScreenOptions] the home screen that shows after login or when re-launching.
     */
    var defaultHomeScreen: HomeScreenOptions
        get() {
            val index = requireNotNull(
                preferences.getString(
                    _defaultHomeScreen,
                    HomeScreenOptions.Anime.index.toString()
                )
            ) {
                "Preferences defaultHomeScreen returned null, with supplied default value"
            }

            return HomeScreenOptions.getFromIndex(index)
        }
        set(value) {
            preferences.edit {
                putString(_defaultHomeScreen, value.index.toString())
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
