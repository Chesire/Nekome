package com.chesire.nekome.core

import android.content.Context
import android.content.SharedPreferences
import com.chesire.nekome.core.flags.UserSeriesStatus
import dagger.Reusable
import javax.inject.Inject

/**
 * Interact with the settings of the application, provides methods to get the user chosen values.
 * These values must be set from the settings fragment within the app-Settings module.
 */
@Reusable
class ApplicationSettings @Inject constructor(
    context: Context,
    private val preferences: SharedPreferences
) {

    private val _defaultSeriesState = context.getString(R.string.key_default_series_state)

    /**
     * Gets the default [UserSeriesStatus] a series should start in when a user begins tracking it.
     */
    val defaultSeriesState: UserSeriesStatus
        get() = UserSeriesStatus.getFromIndex(
            requireNotNull(
                preferences.getString(
                    _defaultSeriesState,
                    UserSeriesStatus.Current.index.toString()
                )
            ) {
                "Preferences defaultSeriesState returned null, with supplied default value"
            }
        )
}
