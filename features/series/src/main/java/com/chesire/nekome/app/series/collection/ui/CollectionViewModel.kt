package com.chesire.nekome.app.series.collection.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.collection.core.CollectSeriesUseCase
import com.chesire.nekome.app.series.collection.core.DeleteSeriesUseCase
import com.chesire.nekome.app.series.collection.core.FilterSeriesUseCase
import com.chesire.nekome.app.series.collection.core.IncrementSeriesUseCase
import com.chesire.nekome.app.series.collection.core.RefreshSeriesUseCase
import com.chesire.nekome.app.series.collection.core.SortSeriesUseCase
import com.chesire.nekome.datasource.series.SeriesDomain
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionViewModel @Inject constructor(
    private val collectSeries: CollectSeriesUseCase,
    private val deleteSeries: DeleteSeriesUseCase,
    private val filterSeries: FilterSeriesUseCase,
    private val incrementSeries: IncrementSeriesUseCase,
    private val refreshSeries: RefreshSeriesUseCase,
    private val sortSeries: SortSeriesUseCase
) : ViewModel() {

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
                .map(filterSeries::invoke)
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
            // TODO: set isRefreshing to true
            refreshSeries()
                .onSuccess {

                }
                .onFailure {
                    state = state.copy(
                        errorSnackbar = SnackbarData(R.string.series_list_refresh_error)
                    )
                }
            // TODO: set isRefreshing to false
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
