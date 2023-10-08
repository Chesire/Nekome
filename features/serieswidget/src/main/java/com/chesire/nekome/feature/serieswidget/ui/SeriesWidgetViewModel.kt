package com.chesire.nekome.feature.serieswidget.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.feature.serieswidget.core.RetrieveSeriesUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class SeriesWidgetViewModel @Inject constructor(
    private val retrieveSeries: RetrieveSeriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            retrieveSeries().collect { series ->
                _uiState.update {
                    Timber.i("Updating")
                    it.copy(
                        series = series.map {
                            Series(
                                userId = it.userId,
                                title = it.title,
                                progress = it.progress.toString(),
                                isUpdating = false
                            )
                        }
                    )
                }
            }
        }
    }

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.UpdateSeries -> handleUpdateSeries(viewAction.id)
        }
    }

    private fun handleUpdateSeries(id: String) {
        // TODO: Hit usecase
    }
}
