package com.chesire.nekome.app.series.item.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.series.R
import com.chesire.nekome.app.series.item.core.BuildTitleUseCase
import com.chesire.nekome.app.series.item.core.DeleteItemUseCase
import com.chesire.nekome.app.series.item.core.RetrieveItemUseCase
import com.chesire.nekome.app.series.item.core.UpdateItemModel
import com.chesire.nekome.app.series.item.core.UpdateItemUseCase
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Note this value is pulled from the nav_graph.xml
private const val SERIES_ID = "seriesId"
private const val MAX_PROGRESS_NUMBERS = 4

@HiltViewModel
class ItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val retrieveItem: RetrieveItemUseCase,
    private val updateItem: UpdateItemUseCase,
    private val deleteItem: DeleteItemUseCase,
    private val buildTitle: BuildTitleUseCase
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
                title = buildTitle(series),
                subtitle = "${series.type.name}  -  ${series.subtype.name}  -  ${series.seriesStatus.name}",
                imageUrl = series.posterImage.medium.url, // TODO: Get quality
                possibleSeriesStatus = UserSeriesStatus
                    .values()
                    .filterNot { it == UserSeriesStatus.Unknown },
                seriesStatus = series.userSeriesStatus,
                progress = series.progress.toString(),
                length = series.totalLength.takeUnless { it == 0 }?.toString() ?: "-",
                rating = series.rating.toFloat()
            )
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.ConfirmPressed -> handleConfirmPressed()
            ViewAction.DeletePressed -> handleDeletePressed()
            ViewAction.SnackbarObserved -> handleSnackbarObserved()
            ViewAction.FinishScreenObserved -> handleFinishScreenObserved()
            is ViewAction.OnDeleteResult -> handleDeleteResult(action.result)
            is ViewAction.ProgressChanged -> handleProgressChanged(action.newProgress)
            is ViewAction.RatingChanged -> handleRatingChanged(action.newRating)
            is ViewAction.SeriesStatusChanged -> handleSeriesStatusChanged(action.newSeriesStatus)
        }
    }

    private fun handleConfirmPressed() = viewModelScope.launch {
        state = state.copy(isSendingData = true)

        updateItem(
            UpdateItemModel(
                userSeriesId = state.id,
                progress = state.progress.toIntOrNull() ?: 0,
                newStatus = state.seriesStatus,
                rating = state.rating.roundToInt()
            )
        ).onSuccess {
            state = state.copy(
                isSendingData = false,
                finishScreen = true
            )
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

    private fun handleDeletePressed() {
        state = state.copy(
            deleteDialog = Delete(
                show = true,
                title = state.title
            )
        )
    }

    private fun handleSnackbarObserved() {
        state = state.copy(errorSnackbar = null)
    }

    private fun handleFinishScreenObserved() {
        state = state.copy(finishScreen = false)
    }

    private fun handleDeleteResult(result: Boolean) {
        if (!result) {
            state = state.copy(deleteDialog = state.deleteDialog.copy(show = false))
            return
        }

        viewModelScope.launch {
            state = state.copy(
                isSendingData = true,
                deleteDialog = state.deleteDialog.copy(show = false)
            )
            deleteItem(state.id)
                .onSuccess {
                    state = state.copy(
                        isSendingData = false,
                        finishScreen = true
                    )
                }
                .onFailure {
                    state = state.copy(
                        isSendingData = false,
                        errorSnackbar = SnackbarData(
                            stringRes = R.string.series_list_delete_failure
                        )
                    )
                }
        }
    }

    private fun handleProgressChanged(newProgress: String) {
        if (newProgress.count() <= MAX_PROGRESS_NUMBERS) {
            state = state.copy(progress = newProgress)
        }
    }

    private fun handleRatingChanged(newRating: Float) {
        state = state.copy(rating = newRating)
    }

    private fun handleSeriesStatusChanged(newStatus: UserSeriesStatus) {
        state = state.copy(seriesStatus = newStatus)
    }
}
