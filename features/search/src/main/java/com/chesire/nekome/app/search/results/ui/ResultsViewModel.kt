package com.chesire.nekome.app.search.results.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.app.search.results.core.RetrieveUserSeriesIdsUseCase
import com.chesire.nekome.app.search.results.core.TrackSeriesUseCase
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
            models = savedStateHandle
                .get<Array<SearchModel>>(SEARCH_RESULTS_KEY)
                ?.toResultModels()
                ?: emptyList(),
            errorSnackbar = null
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
                val newModels = state.models.map {
                    it.copy(canTrack = !userModelIds.contains(it.id))
                }
                state = state.copy(models = newModels)
            }
        }
    }

    fun trackNewSeries(resultModel: ResultModel) {
        viewModelScope.launch {
            state = state.copy(
                models = buildResultState(
                    seriesId = resultModel.id,
                    isTracking = true
                )
            )

            trackSeries(resultModel.id, resultModel.type)
                .onSuccess {
                    state = state.copy(
                        models = buildResultState(
                            seriesId = resultModel.id,
                            isTracking = false
                        )
                    )
                }
                .onFailure {
                    state = state.copy(
                        models = buildResultState(
                            seriesId = resultModel.id,
                            isTracking = false
                        ),
                        errorSnackbar = SnackbarData(
                            R.string.results_failure,
                            resultModel.canonicalTitle
                        )
                    )
                }
        }
    }

    private fun buildResultState(
        seriesId: Int,
        isTracking: Boolean
    ): List<ResultModel> {
        return state.models.map {
            if (it.id == seriesId) {
                it.copy(isTracking = isTracking)
            } else {
                it
            }
        }
    }

    fun errorSnackbarObserved() {
        state = state.copy(errorSnackbar = null)
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
