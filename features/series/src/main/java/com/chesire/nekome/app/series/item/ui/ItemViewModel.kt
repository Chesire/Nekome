package com.chesire.nekome.app.series.item.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.item.core.RetrieveItemUseCase
import com.chesire.nekome.app.series.item.core.UpdateItemModel
import com.chesire.nekome.app.series.item.core.UpdateItemUseCase
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Note this value is pulled from the nav_graph.xml
private const val SERIES_ID = "seriesId"

@HiltViewModel
class ItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val retrieveItem: RetrieveItemUseCase,
    private val updateItem: UpdateItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState.default)
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }

    init {
        viewModelScope.launch {
            val seriesId = requireNotNull(savedStateHandle.get<Int>(SERIES_ID))
            val series = retrieveItem(seriesId)

            state = state.copy(
                id = series.userId,
                title = series.title,
                subtitle = "${series.type.name} - ${series.subtype.name} - ${series.seriesStatus.name}",
                seriesStatus = series.userSeriesStatus,
                progress = series.progress,
                length = series.totalLength.takeUnless { it == 0 }?.toString() ?: "-",
                rating = series.rating
            )
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.CancelPressed -> handleCancelPressed()
            ViewAction.ConfirmPressed -> handleConfirmPressed()
            ViewAction.SnackbarObserved -> handleSnackbarObserved()
            is ViewAction.ProgressChanged -> handleProgressChanged(action.newProgress)
            is ViewAction.RatingChanged -> handleRatingChanged(action.newRating)
            is ViewAction.SeriesStatusChanged -> handleSeriesStatusChanged(action.newSeriesStatus)
        }
    }

    private fun handleCancelPressed() {
        // TODO: Close the screen
    }

    private fun handleConfirmPressed() = viewModelScope.launch {
        state = state.copy(isSendingData = true)
        updateItem(
            UpdateItemModel(
                userSeriesId = state.id,
                progress = state.progress,
                newStatus = state.seriesStatus,
                rating = state.rating
            )
        ).onSuccess {
            // TODO: Show success, maybe close screen?
            state = state.copy(isSendingData = false)
        }.onFailure {
            state = state.copy(
                isSendingData = false,
                errorSnackbar = SnackbarData(
                    stringRes = R.string.series_detail_failure,
                    formatText = state.title
                )
            )
        }
    }

    private fun handleSnackbarObserved() {
        state = state.copy(errorSnackbar = null)
    }

    private fun handleProgressChanged(newProgress: Int) {
        state = state.copy(progress = newProgress)
    }

    private fun handleRatingChanged(newRating: Int) {
        state = state.copy(rating = newRating)
    }

    private fun handleSeriesStatusChanged(newStatus: UserSeriesStatus) {
        state = state.copy(seriesStatus = newStatus)
    }
}

// TODO:
// Move rating out into own composable
// Build screen
// Handle success
