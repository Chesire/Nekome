package com.chesire.nekome.app.login.syncing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.login.syncing.core.RetrieveAvatarUseCase
import com.chesire.nekome.app.login.syncing.core.SyncSeriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel to aid with pulling down the users series via the [SyncingFragment].
 */
@HiltViewModel
class SyncingViewModel @Inject constructor(
    private val retrieveAvatar: RetrieveAvatarUseCase,
    private val syncSeries: SyncSeriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState("", null))
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }

    init {
        viewModelScope.launch {
            state = state.copy(avatar = retrieveAvatar())
        }
        viewModelScope.launch {
            syncSeries()
            state = state.copy(finishedSyncing = true)
        }
    }

    fun observeFinishedSyncing() {
        state = state.copy(finishedSyncing = null)
    }
}
