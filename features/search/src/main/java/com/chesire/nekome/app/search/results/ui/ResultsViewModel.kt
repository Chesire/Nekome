package com.chesire.nekome.app.search.results.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.search.domain.SearchModel
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

// Note this value is pulled from the search_nav_graph.xml
private const val SEARCH_RESULTS_KEY = "searchResults"

@HiltViewModel
class ResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val retrieveUserSeriesIds: RetrieveUserSeriesIdsUseCase,
    private val trackSeries: TrackSeriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UIState(
            savedStateHandle
                .get<Array<SearchModel>>(SEARCH_RESULTS_KEY)
                ?.toResultModels()
                ?: emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }

    init {
        viewModelScope.launch {
            retrieveUserSeriesIds().collectLatest { userModelIds ->
                val models = state.models
                val newModels = models.map {
                    it.copy(canTrack = !userModelIds.contains(it.id))
                }
                state = state.copy(models = newModels)
            }
        }
    }

    fun trackNewSeries(seriesId: Int, seriesType: SeriesType) {
        viewModelScope.launch {
            // TODO: Update UI with the series that is updated
            trackSeries(seriesId, seriesType)
                .onSuccess {
                    // Update view state
                }
                .onFailure {
                    // Update view state
                }
            // TODO: Stop the trackuing on the series chosen
        }
    }

    private fun Array<SearchModel>.toResultModels(): List<ResultModel> {
        return map {
            ResultModel(
                id = it.id,
                type = it.type,
                synopsis = it.synopsis,
                canonicalTitle = it.canonicalTitle,
                subtype = it.subtype.name,
                posterImage = it.posterImage,
                canTrack = false,
                isTracking = false
            )
        }
    }
}
