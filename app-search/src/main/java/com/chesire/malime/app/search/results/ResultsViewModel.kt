package com.chesire.malime.app.search.results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.core.AuthCaster
import com.chesire.malime.core.extensions.postError
import com.chesire.malime.core.extensions.postSuccess
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to aid with adding new series to track that are found through search.
 */
class ResultsViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository,
    private val authCaster: AuthCaster
) : ViewModel() {

    private val _trackSeriesData = MutableLiveData<AsyncState<SeriesModel, ResultsError>>()
    val trackSeriesData: LiveData<AsyncState<SeriesModel, ResultsError>> = _trackSeriesData
    val series = seriesRepo.series

    /**
     * Adds [newSeries] to the list of tracked series.
     */
    fun trackNewSeries(newSeries: SeriesModel) = viewModelScope.launch {
        when (val result = when {
            newSeries.type == SeriesType.Anime -> seriesRepo.addAnime(
                newSeries.id,
                UserSeriesStatus.Current
            )
            newSeries.type == SeriesType.Manga -> seriesRepo.addManga(
                newSeries.id,
                UserSeriesStatus.Current
            )
            else -> error("Unknown SeriesType: ${newSeries.type}")
        }) {
            is Resource.Error -> {
                if (result.code == Resource.Error.CouldNotRefresh) {
                    authCaster.issueRefreshingToken()
                } else {
                    _trackSeriesData.postError(ResultsError.GenericError)
                }
            }
            is Resource.Success -> _trackSeriesData.postSuccess(result.data)
        }
    }
}
