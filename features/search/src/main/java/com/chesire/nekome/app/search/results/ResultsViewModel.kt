package com.chesire.nekome.app.search.results

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.library.SeriesRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel to aid with adding new series to track that are found through search.
 */
class ResultsViewModel @ViewModelInject constructor(
    private val seriesRepo: SeriesRepository,
    private val authCaster: AuthCaster,
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

        if (response is Resource.Error && response.code == Resource.Error.CouldNotRefresh) {
            authCaster.issueRefreshingToken()
        } else {
            callback(response is Resource.Success)
        }
    }
}
