package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.SeriesPreferences
import javax.inject.Inject
import timber.log.Timber

class UpdateFiltersUseCase @Inject constructor(private val pref: SeriesPreferences) {

    suspend operator fun invoke(newFilter: Map<UserSeriesStatus, Boolean>) {
        Timber.d("Updating filters to [$newFilter]")
        pref.updateFilter(
            newFilter
                .map { it.key.index to it.value }
                .toMap()
        )
    }
}
