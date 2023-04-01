package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.ApplicationPreferences
import javax.inject.Inject
import timber.log.Timber

class UpdateDefaultSeriesStateUseCase @Inject constructor(private val pref: ApplicationPreferences) {

    suspend operator fun invoke(newDefaultState: UserSeriesStatus) {
        Timber.i("Updating default series status to [$newDefaultState]")
        pref.updateDefaultSeriesState(newDefaultState)
    }
}
