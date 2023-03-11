package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.UserSeriesStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class CurrentFiltersUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(): Map<UserSeriesStatus, Boolean> =
        pref
            .filter
            .first()
            .map { UserSeriesStatus.getFromIndex(it.key.toString()) to it.value }
            .filterNot { it.first == UserSeriesStatus.Unknown }
            .toMap()
}
