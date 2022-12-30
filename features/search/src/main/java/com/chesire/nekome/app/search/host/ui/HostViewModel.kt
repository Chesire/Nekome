package com.chesire.nekome.app.search.host.ui

import androidx.lifecycle.ViewModel
import com.chesire.nekome.app.search.host.core.HostInitializeUseCase
import com.chesire.nekome.app.search.host.core.RememberSearchGroupUseCase
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HostViewModel @Inject constructor(
    hostInitialize: HostInitializeUseCase,
    private val rememberSearchGroup: RememberSearchGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState("", SearchGroup.Anime))
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
            ViewAction.ExecuteSearch -> TODO()
        }
    }

    private fun handleSearchGroupChanged(newGroup: SearchGroup) {
        rememberSearchGroup(newGroup)
        state = state.copy(searchGroup = newGroup)
    }

    private fun handleSearchTextUpdated(newText: String) {
        state = state.copy(searchText = newText)
    }
}
