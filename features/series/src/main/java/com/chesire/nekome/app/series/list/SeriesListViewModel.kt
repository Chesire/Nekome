package com.chesire.nekome.app.series.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.extensions.postError
import com.chesire.nekome.core.extensions.postSuccess
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.library.SeriesDomain
import com.chesire.nekome.library.SeriesRepository
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to use with the [SeriesListFragment], handles sending updates for a series.
 */
@HiltViewModel
class SeriesListViewModel @Inject constructor(
    private val repo: SeriesRepository
) : ViewModel() {

    val series = repo.getSeries().asLiveData()
    private val _deletionStatus = LiveEvent<AsyncState<SeriesDomain, SeriesListDeleteError>>()
    val deletionStatus: LiveData<AsyncState<SeriesDomain, SeriesListDeleteError>> = _deletionStatus

    private val _refreshStatus = LiveEvent<AsyncState<Any, Any>>()
    val refreshStatus: LiveData<AsyncState<Any, Any>>
        get() = _refreshStatus

    /**
     * Sends an update for a series, will fire [callback] on completion.
     */
    fun updateSeries(
        userSeriesId: Int,
        newProgress: Int,
        newUserSeriesStatus: UserSeriesStatus,
        rating: Float,
        callback: (Resource<SeriesDomain>) -> Unit
    ) = viewModelScope.launch {
        val response = repo.updateSeries(userSeriesId, newProgress, newUserSeriesStatus, rating)
        callback(response)
    }

    /**
     * Sends a delete request to the repository, will notify status on [deletionStatus] with
     * [SeriesListDeleteError.DeletionFailure] if a failure occurs.
     */
    fun deleteSeries(seriesDomain: SeriesDomain) = viewModelScope.launch {
        val response = repo.deleteSeries(seriesDomain)
        if (response is Resource.Error) {
            _deletionStatus.postError(
                seriesDomain,
                SeriesListDeleteError.DeletionFailure
            )
        }
    }

    /**
     * Forces a refresh of all series.
     */
    fun refreshAllSeries() = viewModelScope.launch {
        val syncCommands = listOf(
            repo.refreshAnime(),
            repo.refreshManga()
        )

        if (syncCommands.any { it is Resource.Error }) {
            _refreshStatus.postError(Any())
        } else {
            _refreshStatus.postSuccess(Any())
        }
    }
}
