package com.chesire.nekome.app.series.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.IOContext
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.extensions.postError
import com.chesire.nekome.core.extensions.postLoading
import com.chesire.nekome.core.extensions.postSuccess
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.library.SeriesDomain
import com.chesire.nekome.library.SeriesRepository
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val MODEL_ID = "SeriesDetail_seriesDomain"

/**
 * ViewModel to store the current series detail model, and allow interactions with its data.
 */
@HiltViewModel
class SeriesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: SeriesRepository,
    @IOContext private val ioContext: CoroutineContext
) : ViewModel() {

    val mutableModel = MutableSeriesModel.from(
        requireNotNull(savedStateHandle.get<SeriesDomain>(MODEL_ID)) { "No MODEL_ID in state" }
    )

    private val _updatingStatus = LiveEvent<AsyncState<MutableSeriesModel, SeriesDetailError>>()
    val updatingStatus: LiveData<AsyncState<MutableSeriesModel, SeriesDetailError>>
        get() = _updatingStatus

    /**
     * Sends an update request with the new information in [target].
     */
    fun sendUpdate(target: MutableSeriesModel) {
        _updatingStatus.postLoading()

        viewModelScope.launch(ioContext) {
            val response = repo.updateSeries(
                target.userSeriesId,
                target.seriesProgress,
                target.userSeriesStatus,
                target.rating
            )
            if (response is Resource.Error) {
                _updatingStatus.postError(
                    target,
                    SeriesDetailError.Error
                )
            } else {
                _updatingStatus.postSuccess(target)
            }
        }
    }
}
