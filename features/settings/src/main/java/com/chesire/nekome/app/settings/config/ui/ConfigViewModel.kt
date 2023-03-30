package com.chesire.nekome.app.settings.config.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.settings.config.core.RetrievePreferencesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultHomeScreenUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultSeriesStateUseCase
import com.chesire.nekome.app.settings.config.core.UpdateRateSeriesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateThemeUseCase
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
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
    private val updateTheme: UpdateThemeUseCase,
    private val updateDefaultHomeScreen: UpdateDefaultHomeScreenUseCase,
    private val updateDefaultSeriesState: UpdateDefaultSeriesStateUseCase
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
                    themeValue = prefModel.theme,
                    defaultHomeValue = prefModel.defaultHomeScreen,
                    defaultSeriesStatusValue = prefModel.defaultSeriesStatus,
                    rateSeriesValue = prefModel.shouldRateSeries
                )
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.OnThemeClicked -> handleOnThemeClicked()
            is ViewAction.OnThemeChanged -> handleOnThemeChanged(action.newTheme)
            ViewAction.OnDefaultHomeScreenClicked -> handleOnDefaultHomeScreenClicked()
            is ViewAction.OnDefaultHomeScreenChanged -> handleOnDefaultHomeScreenChanged(action.newHomeScreen)
            ViewAction.onDefaultSeriesStatusClicked -> handleOnDefaultSeriesStatusClicked()
            is ViewAction.OnDefaultSeriesStatusChanged -> handleOnDefaultSeriesStatusChanged(action.newDefaultSeriesStatus)
            is ViewAction.OnRateSeriesChanged -> handleOnRateSeriesChanged(action.newValue)
        }
    }

    private fun handleOnThemeClicked() {
        state = state.copy(showThemeDialog = true)
    }

    private fun handleOnThemeChanged(newTheme: Theme?) {
        state = state.copy(showThemeDialog = false)
        if (newTheme != null) {
            viewModelScope.launch {
                updateTheme(newTheme)
            }
        }
    }

    private fun handleOnDefaultHomeScreenClicked() {
        state = state.copy(showDefaultHomeDialog = true)
    }

    private fun handleOnDefaultHomeScreenChanged(newHomeScreen: HomeScreenOptions?) {
        state = state.copy(showDefaultHomeDialog = false)
        if (newHomeScreen != null) {
            viewModelScope.launch {
                updateDefaultHomeScreen(newHomeScreen)
            }
        }
    }

    private fun handleOnDefaultSeriesStatusClicked() {
        state = state.copy(showDefaultSeriesStatusDialog = true)
    }

    private fun handleOnDefaultSeriesStatusChanged(newSeriesStatus: UserSeriesStatus?) {
        state = state.copy(showDefaultSeriesStatusDialog = false)
        if (newSeriesStatus != null) {
            viewModelScope.launch {
                updateDefaultSeriesState(newSeriesStatus)
            }
        }
    }

    private fun handleOnRateSeriesChanged(newValue: Boolean) {
        viewModelScope.launch {
            updateRateSeries(newValue)
        }
    }
}
