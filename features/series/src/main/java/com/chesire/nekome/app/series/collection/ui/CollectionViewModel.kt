package com.chesire.nekome.app.series.collection.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.collection.core.CollectSeriesUseCase
import com.chesire.nekome.app.series.collection.core.FilterSeriesUseCase
import com.chesire.nekome.app.series.collection.core.IncrementSeriesUseCase
import com.chesire.nekome.app.series.collection.core.RefreshSeriesUseCase
import com.chesire.nekome.app.series.collection.core.SortSeriesUseCase
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.datasource.series.SeriesDomain
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Note this value is pulled from the nav_graph.xml
private const val SERIES_TYPE = "seriesType"

@HiltViewModel
class CollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val collectSeries: CollectSeriesUseCase,
    private val filterSeries: FilterSeriesUseCase,
    private val incrementSeries: IncrementSeriesUseCase,
    private val refreshSeries: RefreshSeriesUseCase,
    private val sortSeries: SortSeriesUseCase
) : ViewModel() {

    private val seriesType = requireNotNull(savedStateHandle.get<SeriesType>(SERIES_TYPE))
    private val _uiState = MutableStateFlow(
        UIState(
            models = emptyList(),
            isRefreshing = false,
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
            collectSeries()
                .map { filterSeries(it, seriesType) }
                .map(sortSeries::invoke)
                .collectLatest { newModels ->
                    state = state.copy(models = newModels)
                }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.PerformSeriesRefresh -> handleSeriesRefresh()
            is ViewAction.IncrementSeriesPressed -> handleIncrementSeries(action.seriesDomain)
            ViewAction.ErrorSnackbarObserved -> handleErrorSnackbarObserved()
        }
    }

    private fun handleSeriesRefresh() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            refreshSeries()
                .onSuccess {
                    state = state.copy(isRefreshing = false)
                }
                .onFailure {
                    state = state.copy(
                        isRefreshing = false,
                        errorSnackbar = SnackbarData(R.string.series_list_refresh_error)
                    )
                }
        }
    }

    private fun handleIncrementSeries(domain: SeriesDomain) {
        // TODO: Need to check if this would complete the series, if it would show the rating dialog
        viewModelScope.launch {
            incrementSeries(domain)
                .onSuccess {

                }
                .onFailure {
                    state = state.copy(
                        errorSnackbar = SnackbarData(
                            stringRes = R.string.series_list_try_again,
                            formatText = domain.title
                        )
                    )
                }
        }
    }

    private fun handleErrorSnackbarObserved() {
        state = state.copy(errorSnackbar = null)
    }
}
