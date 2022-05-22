package com.chesire.nekome.app.search.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.datasource.series.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel to aid with adding new series to track that are found through search.
 */
@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository,
    private val settings: ApplicationSettings
) : ViewModel() {

    /**
     * A list of series IDs.
     */
    val seriesIds = seriesRepo
        .getSeries()
        .map { it.map { it.id } }
        .asLiveData()

    /**
     * Adds [data] to the list of tracked series.
     */
    fun trackNewSeries(
        data: ResultsData,
        callback: (successful: Boolean) -> Unit
    ) = viewModelScope.launch {
        val response = when (data.type) {
            SeriesType.Anime -> seriesRepo.addAnime(
                data.id,
                settings.defaultSeriesState
            )
            SeriesType.Manga -> seriesRepo.addManga(
                data.id,
                settings.defaultSeriesState
            )
            else -> error("Unknown SeriesType: ${data.type}")
        }

        callback(response is Resource.Success)
    }
}
