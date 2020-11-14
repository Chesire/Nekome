package com.chesire.nekome.core.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chesire.nekome.core.R
import com.chesire.nekome.core.flags.HomeScreenOptions
import com.chesire.nekome.core.flags.UserSeriesStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Interact with the settings of the application, provides methods to get the user chosen values.
 * These values must be set from the settings fragment within the app-Settings module.
 */
@Suppress("UseDataClass")
class ApplicationSettings @Inject constructor(
    @ApplicationContext context: Context,
    private val preferences: SharedPreferences
) {

    private val _defaultSeriesState = context.getString(R.string.key_default_series_state)
    private val _defaultHomeScreen = context.getString(R.string.key_default_home)
    private val _theme = context.getString(R.string.key_theme)

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
    val defaultHomeScreen: HomeScreenOptions
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

    /**
     * Gets the [Theme] the user has chosen to use for the application.
     */
    val theme: Theme
        get() = Theme.fromValue(
            requireNotNull(
                preferences.getString(
                    _theme,
                    Theme.System.value.toString()
                )
            ) {
                "Preferences theme returned null, with supplied default value"
            }
        )
}
