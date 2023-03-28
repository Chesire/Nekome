package com.chesire.nekome.app.settings.config.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.settings.config.core.RetrievePreferencesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateRateSeriesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateThemeUseCase
import com.chesire.nekome.core.preferences.flags.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val retrievePreferences: RetrievePreferencesUseCase,
    private val updateRateSeries: UpdateRateSeriesUseCase,
    private val updateTheme: UpdateThemeUseCase
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
            retrievePreferences().collect { prefModel ->
                state = state.copy(
                    themeStringDisplay = prefModel.theme.stringId,
                    rateSeriesValue = prefModel.shouldRateSeries
                )
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            is ViewAction.OnRateSeriesChanged -> handleOnRateSeriesChanged(action.newValue)
            is ViewAction.OnThemeChanged -> handleOnThemeChanged(action.newTheme) // TODO: Implement the choose theme flow, need to send the list of themes to show and show in a dialog
        }
    }

    private fun handleOnRateSeriesChanged(newValue: Boolean) {
        viewModelScope.launch {
            updateRateSeries(newValue)
        }
    }

    private fun handleOnThemeChanged(newTheme: Theme) {
        viewModelScope.launch {
            updateTheme(newTheme)
        }
    }
}
