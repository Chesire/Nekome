package com.chesire.nekome.app.search.host.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.host.core.HostInitializeUseCase
import com.chesire.nekome.app.search.host.core.RememberSearchGroupUseCase
import com.chesire.nekome.app.search.host.core.SearchFailureReason
import com.chesire.nekome.app.search.host.core.SearchSeriesUseCase
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HostViewModel @Inject constructor(
    hostInitialize: HostInitializeUseCase,
    private val rememberSearchGroup: RememberSearchGroupUseCase,
    private val searchSeries: SearchSeriesUseCase
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
    }

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.SearchGroupChanged -> handleSearchGroupChanged(viewAction.newGroup)
            is ViewAction.SearchTextUpdated -> handleSearchTextUpdated(viewAction.newSearchText)
            ViewAction.ExecuteSearch -> handleExecuteSearch()
            ViewAction.ErrorSnackbarObserved -> handleErrorSnackbarObserved()
            ViewAction.NavigationObserved -> handleNavigationObserved()
        }
    }

    private fun handleSearchGroupChanged(newGroup: SearchGroup) {
        rememberSearchGroup(newGroup)
        state = state.copy(searchGroup = newGroup)
    }

    private fun handleSearchTextUpdated(newText: String) {
        state = state.copy(searchText = newText)
    }

    private fun handleExecuteSearch() {
        val searchCriteria = state.searchText

        viewModelScope.launch {
            state = state.copy(isSearching = true)

            searchSeries(searchCriteria, state.searchGroup)
                .onSuccess {
                    state = state.copy(
                        isSearching = false,
                        navigateScreenEvent = NavigationData(searchCriteria, it)
                    )
                }
                .onFailure { failureReason ->
                    val errorStringRes = when (failureReason) {
                        SearchFailureReason.InvalidTitle -> R.string.search_error_no_text
                        SearchFailureReason.NetworkError -> R.string.error_generic
                        SearchFailureReason.NoSeriesFound -> R.string.search_error_no_series_found
                    }
                    state = state.copy(
                        isSearching = false,
                        errorSnackbarMessage = errorStringRes
                    )
                }
        }
    }

    private fun handleErrorSnackbarObserved() {
        state = state.copy(errorSnackbarMessage = null)
    }

    private fun handleNavigationObserved() {
        state = state.copy(navigateScreenEvent = null)
    }
}
