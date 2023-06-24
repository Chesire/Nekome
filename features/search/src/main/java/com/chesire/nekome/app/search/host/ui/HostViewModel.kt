package com.chesire.nekome.app.search.host.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.host.core.HostInitializeUseCase
import com.chesire.nekome.app.search.host.core.RememberSearchGroupUseCase
import com.chesire.nekome.app.search.host.core.RetrieveUserSeriesIdsUseCase
import com.chesire.nekome.app.search.host.core.SearchFailureReason
import com.chesire.nekome.app.search.host.core.SearchSeriesUseCase
import com.chesire.nekome.app.search.host.core.TrackSeriesUseCase
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HostViewModel @Inject constructor(
    hostInitialize: HostInitializeUseCase,
    private val retrieveUserSeriesIds: RetrieveUserSeriesIdsUseCase,
    private val rememberSearchGroup: RememberSearchGroupUseCase,
    private val searchSeries: SearchSeriesUseCase,
    private val trackSeries: TrackSeriesUseCase,
    private val mapper: DomainMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState.Default)
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }

    init {
        val initializeResult = hostInitialize()
        state = state.copy(
            searchGroup = initializeResult.initialGroup
        )
        viewModelScope.launch {
            retrieveUserSeriesIds().collectLatest { userModelIds ->
                val newModels = state.resultModels.map {
                    it.copy(canTrack = !userModelIds.contains(it.id))
                }
                state = state.copy(resultModels = newModels)
            }
        }
    }

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.SearchGroupChanged -> handleSearchGroupChanged(viewAction.newGroup)
            is ViewAction.SearchTextUpdated -> handleSearchTextUpdated(viewAction.newSearchText)
            ViewAction.ExecuteSearch -> handleExecuteSearch()
            is ViewAction.TrackSeries -> handleTrackSeries(viewAction.model)
            ViewAction.ErrorSnackbarObserved -> handleErrorSnackbarObserved()
        }
    }

    private fun handleSearchGroupChanged(newGroup: SearchGroup) {
        rememberSearchGroup(newGroup)
        state = state.copy(searchGroup = newGroup)
    }

    private fun handleSearchTextUpdated(newText: String) {
        state = state.copy(
            searchText = newText,
            isSearchTextError = false
        )
    }

    private fun handleExecuteSearch() {
        val searchCriteria = state.searchText

        viewModelScope.launch {
            state = state.copy(isSearching = true)

            searchSeries(searchCriteria, state.searchGroup)
                .onSuccess {
                    state = state.copy(
                        isSearching = false,
                        resultModels = mapper.toResultDomain(it, retrieveUserSeriesIds().first())
                    )
                }
                .onFailure(::handleSearchFailure)
        }
    }

    private fun handleSearchFailure(failureReason: SearchFailureReason) {
        state = when (failureReason) {
            SearchFailureReason.InvalidTitle -> state.copy(
                isSearching = false,
                isSearchTextError = true,
                errorSnackbar = SnackbarData(R.string.search_error_no_text)
            )

            SearchFailureReason.NetworkError -> state.copy(
                isSearching = false,
                errorSnackbar = SnackbarData(R.string.error_generic)
            )

            SearchFailureReason.NoSeriesFound -> state.copy(
                isSearching = false,
                errorSnackbar = SnackbarData(R.string.search_error_no_series_found)
            )
        }
    }

    private fun handleTrackSeries(model: ResultModel) {
        viewModelScope.launch {
            state = state.copy(
                resultModels = updateModelsState(
                    seriesId = model.id,
                    isTracking = true
                )
            )

            trackSeries(model.id, model.type)
                .onSuccess {
                    state = state.copy(
                        resultModels = updateModelsState(
                            seriesId = model.id,
                            isTracking = false,
                            canTrack = false
                        )
                    )
                }
                .onFailure {
                    state = state.copy(
                        resultModels = updateModelsState(
                            seriesId = model.id,
                            isTracking = false
                        ),
                        errorSnackbar = SnackbarData(
                            R.string.results_failure,
                            model.canonicalTitle
                        )
                    )
                }
        }
    }

    private fun updateModelsState(
        seriesId: Int,
        isTracking: Boolean,
        canTrack: Boolean? = null
    ): List<ResultModel> {
        return state.resultModels.map {
            if (it.id == seriesId) {
                it.copy(
                    canTrack = canTrack ?: it.canTrack,
                    isTracking = isTracking
                )
            } else {
                it
            }
        }
    }

    private fun handleErrorSnackbarObserved() {
        state = state.copy(errorSnackbar = null)
    }
}
