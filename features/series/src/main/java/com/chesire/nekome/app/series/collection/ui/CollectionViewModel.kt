@file:OptIn(FlowPreview::class)

package com.chesire.nekome.app.series.collection.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.collection.core.CollectSeriesUseCase
import com.chesire.nekome.app.series.collection.core.FilterSeriesUseCase
import com.chesire.nekome.app.series.collection.core.IncrementSeriesUseCase
import com.chesire.nekome.app.series.collection.core.RefreshSeriesUseCase
import com.chesire.nekome.app.series.collection.core.ShouldRateSeriesUseCase
import com.chesire.nekome.app.series.collection.core.SortSeriesUseCase
import com.chesire.nekome.app.series.collection.core.UpdateSortUseCase
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.SortOption
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Note this value is pulled from the nav_graph.xml
private const val SERIES_TYPE = "seriesType"

// TODO: Menu items - need to show filter and sort
// TODO: Find out how to launch the details bottom sheet, for now maybe just launch the fragment?

@HiltViewModel
class CollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    collectSeries: CollectSeriesUseCase,
    private val filterSeries: FilterSeriesUseCase,
    private val incrementSeries: IncrementSeriesUseCase,
    private val refreshSeries: RefreshSeriesUseCase,
    private val shouldRateSeries: ShouldRateSeriesUseCase,
    private val sortSeries: SortSeriesUseCase,
    private val updateSort: UpdateSortUseCase,
    private val domainMapper: DomainMapper
) : ViewModel() {

    private val _seriesType = requireNotNull(savedStateHandle.get<SeriesType>(SERIES_TYPE))
    private val _uiState = MutableStateFlow(
        UIState(
            models = emptyList(),
            isRefreshing = false,
            ratingDialog = null,
            errorSnackbar = null,
            seriesDetails = null,
            sortDialog = Sort(
                show = false,
                sortOptions = listOf(
                    SortOption.Default,
                    SortOption.Title,
                    SortOption.StartDate,
                    SortOption.EndDate,
                    SortOption.Rating
                )
            )
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
                .map { filterSeries(it, _seriesType) }
                .map(sortSeries::invoke)
                .flattenConcat()
                .map(domainMapper::toSeries)
                .collectLatest { newModels ->
                    state = state.copy(models = newModels)
                }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.PerformSeriesRefresh -> handleSeriesRefresh()
            is ViewAction.IncrementSeriesPressed -> handleIncrementSeries(action.series)
            is ViewAction.IncrementSeriesWithRating -> handleIncrementSeriesWithRating(
                action.series,
                action.rating
            )
            ViewAction.FilterPressed -> TODO()
            ViewAction.SortPressed -> handleSortPressed()
            is ViewAction.PerformSort -> handlePerformSort(action.option)
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

    private fun handleIncrementSeries(series: Series) {
        viewModelScope.launch {
            if (shouldRateSeries(series.userId)) {
                state = state.copy(ratingDialog = Rating(series, true))
            } else {
                invokeIncrementSeries(series, null)
            }
        }
    }

    private fun handleIncrementSeriesWithRating(series: Series, rating: Int?) {
        viewModelScope.launch {
            invokeIncrementSeries(series, rating)
        }
    }

    private fun invokeIncrementSeries(series: Series, rating: Int?) {
        state = state.copy(models = updateIsUpdating(series.userId, true))

        viewModelScope.launch {
            incrementSeries(series.userId, rating)
                .onSuccess {
                    state = state.copy(models = updateIsUpdating(series.userId, false))
                }
                .onFailure {
                    state = state.copy(
                        models = updateIsUpdating(series.userId, false),
                        errorSnackbar = SnackbarData(
                            stringRes = R.string.series_list_try_again,
                            formatText = series.title
                        )
                    )
                }
        }
    }

    private fun updateIsUpdating(id: Int, isUpdating: Boolean): List<Series> {
        return state.models.map {
            if (it.userId == id) {
                it.copy(isUpdating = isUpdating)
            } else {
                it
            }
        }
    }

    private fun handleSortPressed() {
        state = state.copy(
            sortDialog = Sort(
                show = true,
                sortOptions = listOf(
                    SortOption.Default,
                    SortOption.Title,
                    SortOption.StartDate,
                    SortOption.EndDate,
                    SortOption.Rating
                )
            )
        )
    }

    private fun handlePerformSort(sortOption: SortOption?) = viewModelScope.launch {
        if (sortOption != null) {
            updateSort(sortOption)
        }
        // SortSeries use case should listen to the preferences, and update when its updated?
        state = state.copy(sortDialog = state.sortDialog.copy(show = false))
    }

    private fun handleErrorSnackbarObserved() {
        state = state.copy(errorSnackbar = null)
    }
}
