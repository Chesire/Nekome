package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import javax.inject.Inject
import timber.log.Timber

class UpdateDefaultHomeScreenUseCase @Inject constructor(private val pref: ApplicationPreferences) {

    suspend operator fun invoke(newHomeScreen: HomeScreenOptions) {
        Timber.i("Updating default home screen to [$newHomeScreen]")
        pref.updateDefaultHomeScreen(newHomeScreen)
    }
}
