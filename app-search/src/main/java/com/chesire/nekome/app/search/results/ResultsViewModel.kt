package com.chesire.nekome.app.search.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.ApplicationSettings
import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.series.SeriesRepository
import com.chesire.nekome.server.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to aid with adding new series to track that are found through search.
 */
class ResultsViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository,
    private val authCaster: AuthCaster,
    private val settings: ApplicationSettings
) : ViewModel() {

    val series = seriesRepo.getSeries().asLiveData()

    /**
     * Adds [newSeries] to the list of tracked series.
     */
    fun trackNewSeries(
        newSeries: SeriesModel,
        callback: (Resource<SeriesModel>) -> Unit
    ) = viewModelScope.launch {
        val response = when (newSeries.type) {
            SeriesType.Anime -> seriesRepo.addAnime(
                newSeries.id,
                settings.defaultSeriesState
            )
            SeriesType.Manga -> seriesRepo.addManga(
                newSeries.id,
                settings.defaultSeriesState
            )
            else -> error("Unknown SeriesType: ${newSeries.type}")
        }

        if (response is Resource.Error && response.code == Resource.Error.CouldNotRefresh) {
            authCaster.issueRefreshingToken()
        } else {
            callback(response)
        }
    }
}
