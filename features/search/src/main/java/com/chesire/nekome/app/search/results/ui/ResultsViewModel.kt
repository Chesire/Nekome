package com.chesire.nekome.app.search.results.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.search.results.core.RetrieveUserSeriesIdsUseCase
import com.chesire.nekome.app.search.results.core.TrackSeriesUseCase
import com.chesire.nekome.core.flags.SeriesType
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val retrieveUserSeriesIds: RetrieveUserSeriesIdsUseCase,
    private val trackSeries: TrackSeriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState(1))
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }


    init {
        // pull the models out of the savedStateHandle
        viewModelScope.launch {
            retrieveUserSeriesIds().collectLatest {
                // update the series
            }
        }
    }

    fun trackNewSeries(seriesId: Int, seriesType: SeriesType) {
        viewModelScope.launch {
            trackSeries(seriesId, seriesType)
                .onSuccess {
                    // Update view state
                }
                .onFailure {
                    // Update view state
                }
        }
    }
}
