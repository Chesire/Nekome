package com.chesire.nekome.app.series.collection.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.series.collection.core.CollectSeriesUseCase
import com.chesire.nekome.app.series.collection.core.DeleteSeriesUseCase
import com.chesire.nekome.app.series.collection.core.FilterSeriesUseCase
import com.chesire.nekome.app.series.collection.core.RefreshSeriesUseCase
import com.chesire.nekome.app.series.collection.core.SortSeriesUseCase
import com.chesire.nekome.app.series.collection.core.UpdateSeriesUseCase
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
    private val refreshSeries: RefreshSeriesUseCase,
    private val sortSeries: SortSeriesUseCase,
    private val updateSeries: UpdateSeriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState(models = emptyList()))
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
}
