package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class BuildTitleUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(series: SeriesDomain): String {
        return when (val titleLanguage = pref.titleLanguage.first()) {
            TitleLanguage.Canonical -> series.title
            else -> series.otherTitles[titleLanguage.key] ?: series.title
        }
    }
}
