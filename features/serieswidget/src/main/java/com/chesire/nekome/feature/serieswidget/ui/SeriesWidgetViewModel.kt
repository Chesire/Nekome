package com.chesire.nekome.feature.serieswidget.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.feature.serieswidget.core.RetrieveSeriesUseCase
import com.chesire.nekome.feature.serieswidget.core.UpdateSeriesUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeriesWidgetViewModel @Inject constructor(
    private val retrieveSeries: RetrieveSeriesUseCase,
    private val updateSeries: UpdateSeriesUseCase,
    private val mapper: DomainMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            retrieveSeries().collect { series ->
                _uiState.update {
                    it.copy(series = series.map(mapper::toSeries))
                }
            }
        }
    }

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.UpdateSeries -> handleUpdateSeries(viewAction.id)
        }
    }

    private fun handleUpdateSeries(id: Int) {
        viewModelScope.launch {
            updateSeries(id)
        }
    }
}
