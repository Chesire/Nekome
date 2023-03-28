package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.flags.Theme
import javax.inject.Inject
import timber.log.Timber

class UpdateThemeUseCase @Inject constructor(private val pref: ApplicationPreferences) {

    suspend operator fun invoke(newTheme: Theme) {
        Timber.i("Updating theme to [$newTheme]")
        pref.updateTheme(newTheme)
    }
}
