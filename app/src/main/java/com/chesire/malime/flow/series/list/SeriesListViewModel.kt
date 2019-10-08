package com.chesire.malime.flow.series.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.AuthCaster
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to use with the [SeriesListFragment], handles sending updates for a series.
 */
class SeriesListViewModel @Inject constructor(
    private val repo: SeriesRepository,
    private val authCaster: AuthCaster
) : ViewModel() {
    val animeSeries = repo.anime
    val mangaSeries = repo.manga

    private val _deletionStatus = LiveEvent<SeriesListDeleteError>()
    val deletionStatus: LiveData<SeriesListDeleteError> = _deletionStatus

    /**
     * Sends an update for a series, will fire [callback] on completion.
     */
    fun updateSeries(
        userSeriesId: Int,
        newProgress: Int,
        newUserSeriesStatus: UserSeriesStatus,
        callback: (Resource<SeriesModel>) -> Unit
    ) = viewModelScope.launch {
        val response = repo.updateSeries(userSeriesId, newProgress, newUserSeriesStatus)
        if (response is Resource.Error && response.code == Resource.Error.CouldNotRefresh) {
            authCaster.issueRefreshingToken()
        } else {
            callback(response)
        }
    }

    /**
     * Sends a delete request to the repository, will notify status on [deletionStatus] with
     * [SeriesListDeleteError.DeletionFailure] if a failure occurs.
     */
    fun deleteSeries(seriesModel: SeriesModel) = viewModelScope.launch {
        val response = repo.deleteSeries(seriesModel)
        if (response is Resource.Error) {
            if (response.code == Resource.Error.CouldNotRefresh) {
                authCaster.issueRefreshingToken()
            } else {
                _deletionStatus.postValue(SeriesListDeleteError.DeletionFailure)
            }
        }
    }
}
