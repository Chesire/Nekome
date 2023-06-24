package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import javax.inject.Inject
import timber.log.Timber

class UpdateTitleLanguageUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(newTitleLanguage: TitleLanguage) {
        Timber.i("Updating title language to [$newTitleLanguage]")
        pref.updateTitleLanguage(newTitleLanguage)
    }
}
