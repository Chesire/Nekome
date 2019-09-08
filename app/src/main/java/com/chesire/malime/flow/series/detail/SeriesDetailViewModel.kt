package com.chesire.malime.flow.series.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.AuthCaster
import com.chesire.malime.core.IOContext
import com.chesire.malime.core.extensions.postError
import com.chesire.malime.core.extensions.postLoading
import com.chesire.malime.core.extensions.postSuccess
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel to store the current series detail model, and allow interactions with its data.
 */
class SeriesDetailViewModel @Inject constructor(
    private val repo: SeriesRepository,
    private val authCaster: AuthCaster,
    @IOContext private val ioContext: CoroutineContext
) : ViewModel() {

    private val _model = MutableLiveData<SeriesModel>()
    val model: LiveData<SeriesModel> = _model
    private val _deletionStatus = LiveEvent<AsyncState<SeriesModel, SeriesDetailError>>()
    val deletionStatus: LiveData<AsyncState<SeriesModel, SeriesDetailError>> = _deletionStatus
    private val _progressStatus = LiveEvent<AsyncState<SeriesModel, SeriesDetailError>>()
    val progressStatus: LiveData<AsyncState<SeriesModel, SeriesDetailError>> = _progressStatus

    /**
     * Updates the currently stored model in the view model.
     */
    fun updateModel(newModel: SeriesModel) = _model.postValue(newModel)

    /**
     * Sends a delete request for the [target].
     */
    fun deleteModel(target: SeriesModel) {
        _deletionStatus.postLoading()

        viewModelScope.launch(ioContext) {
            val response = repo.deleteSeries(target)
            if (response is Resource.Error && response.code == Resource.Error.CouldNotRefresh) {
                authCaster.issueRefreshingToken()
            } else if (response is Resource.Error) {
                _deletionStatus.postError(target, SeriesDetailError.Error)
            } else {
                _deletionStatus.postSuccess(target)
            }
        }
    }

    fun updateProgress(target: SeriesModel, newProgress: Int) {
        _progressStatus.postLoading()

        viewModelScope.launch(ioContext) {
            val response = repo.updateSeries(target.userId, newProgress, target.userSeriesStatus)
            if (response is Resource.Error && response.code == Resource.Error.CouldNotRefresh) {
                authCaster.issueRefreshingToken()
            } else if (response is Resource.Error) {
                _progressStatus.postError(target, SeriesDetailError.Error)
            } else {
                _progressStatus.postSuccess(target)
            }
        }
    }

    fun checkProgressValue(target: SeriesModel, newProgress: Int?): SeriesDetailError {
        return when {
            newProgress == null -> SeriesDetailError.NewProgressNaN
            newProgress < 0 -> SeriesDetailError.NewProgressBelowZero
            target.lengthKnown && newProgress > target.totalLength -> SeriesDetailError.NewProgressTooHigh
            else -> return SeriesDetailError.None
        }
    }
}
