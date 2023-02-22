@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.collection.core.CollectSeriesUseCase
import com.chesire.nekome.app.series.collection.core.CurrentFiltersUseCase
import com.chesire.nekome.app.series.collection.core.CurrentSortUseCase
import com.chesire.nekome.app.series.collection.core.FilterSeriesUseCase
import com.chesire.nekome.app.series.collection.core.IncrementSeriesUseCase
import com.chesire.nekome.app.series.collection.core.RefreshSeriesUseCase
import com.chesire.nekome.app.series.collection.core.ShouldRateSeriesUseCase
import com.chesire.nekome.app.series.collection.core.SortSeriesUseCase
import com.chesire.nekome.app.series.collection.core.UpdateFiltersUseCase
import com.chesire.nekome.app.series.collection.core.UpdateSortUseCase
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.SortOption
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Note this value is pulled from the nav_graph.xml
private const val SERIES_TYPE = "seriesType"

// TODO: Find out how to launch the details bottom sheet, for now maybe just launch the fragment?
// TODO: Show a "loading" screen, which should be a list of shimmering items

@HiltViewModel
class CollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    collectSeries: CollectSeriesUseCase,
    private val incrementSeries: IncrementSeriesUseCase,
    private val refreshSeries: RefreshSeriesUseCase,
    private val shouldRateSeries: ShouldRateSeriesUseCase,
    private val currentSort: CurrentSortUseCase,
    private val sortSeries: SortSeriesUseCase,
    private val updateSort: UpdateSortUseCase,
    private val currentFilters: CurrentFiltersUseCase,
    private val filterSeries: FilterSeriesUseCase,
    private val updateFilters: UpdateFiltersUseCase,
    private val domainMapper: DomainMapper
) : ViewModel() {

    private val _seriesType = requireNotNull(savedStateHandle.get<SeriesType>(SERIES_TYPE))
    private val _uiState = MutableStateFlow(UIState.default)
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }

    init {
        viewModelScope.launch {
            collectSeries()
                .flatMapLatest { filterSeries(it, _seriesType) }
                .flatMapLatest { sortSeries(it) }
                .map(domainMapper::toSeries)
                .collect { newModels ->
                    state = state.copy(models = newModels)
                }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.PerformSeriesRefresh -> handleSeriesRefresh()
            is ViewAction.SeriesPressed -> handleSeriesPressed(action.series)
            is ViewAction.IncrementSeriesPressed -> handleIncrementSeries(action.series)
            is ViewAction.IncrementSeriesWithRating -> handleIncrementSeriesWithRating(
                action.series,
                action.rating
            )
            ViewAction.SortPressed -> handleSortPressed()
            is ViewAction.PerformSort -> handlePerformSort(action.option)
            ViewAction.FilterPressed -> handleFilterPressed()
            is ViewAction.PerformFilter -> handlePerformFilter(action.filters)
            ViewAction.ErrorSnackbarObserved -> handleErrorSnackbarObserved()
        }
    }

    private fun handleSeriesRefresh() = viewModelScope.launch {
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

    private fun handleSeriesPressed(series: Series) {
        state = state.copy(
            seriesDetails = SeriesDetails(
                show = true,
                seriesId = series.userId
            )
        )
    }

    private fun handleIncrementSeries(series: Series) = viewModelScope.launch {
        if (shouldRateSeries(series.userId)) {
            state = state.copy(ratingDialog = Rating(series, true))
        } else {
            invokeIncrementSeries(series, null)
        }
    }

    private fun handleIncrementSeriesWithRating(series: Series, rating: Int?) =
        viewModelScope.launch {
            invokeIncrementSeries(series, rating)
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

    private fun handleSortPressed() = viewModelScope.launch {
        state = state.copy(
            sortDialog = state.sortDialog.copy(
                show = true,
                currentSort = currentSort()
            )
        )
    }

    private fun handlePerformSort(sortOption: SortOption?) = viewModelScope.launch {
        if (sortOption != null) {
            updateSort(sortOption)
        }
        state = state.copy(sortDialog = state.sortDialog.copy(show = false))
    }

    private fun handleFilterPressed() = viewModelScope.launch {
        state = state.copy(
            filterDialog = state.filterDialog.copy(
                show = true,
                filterOptions = currentFilters()
                    .toSortedMap(compareBy { it.index })
                    .map {
                        FilterOption(
                            userStatus = it.key,
                            selected = it.value
                        )
                    }
            )
        )
    }

    private fun handlePerformFilter(filterOptions: List<FilterOption>?) = viewModelScope.launch {
        if (filterOptions != null) {
            updateFilters(filterOptions.associate { it.userStatus to it.selected })
        }
        state = state.copy(filterDialog = state.filterDialog.copy(show = false))
    }

    private fun handleErrorSnackbarObserved() {
        state = state.copy(errorSnackbar = null)
    }
}
