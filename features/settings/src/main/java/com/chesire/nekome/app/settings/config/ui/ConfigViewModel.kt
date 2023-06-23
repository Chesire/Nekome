package com.chesire.nekome.app.settings.config.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.settings.config.LogoutExecutor
import com.chesire.nekome.app.settings.config.core.RetrievePreferencesUseCase
import com.chesire.nekome.app.settings.config.core.RetrieveUserUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultHomeScreenUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultSeriesStateUseCase
import com.chesire.nekome.app.settings.config.core.UpdateImageQualityUseCase
import com.chesire.nekome.app.settings.config.core.UpdateRateSeriesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateThemeUseCase
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.Theme
import com.chesire.nekome.datasource.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val retrievePreferences: RetrievePreferencesUseCase,
    private val retrieveUser: RetrieveUserUseCase,
    private val updateRateSeries: UpdateRateSeriesUseCase,
    private val updateTheme: UpdateThemeUseCase,
    private val updateDefaultHomeScreen: UpdateDefaultHomeScreenUseCase,
    private val updateDefaultSeriesState: UpdateDefaultSeriesStateUseCase,
    private val updateImageQuality: UpdateImageQualityUseCase,
    private val logoutExecutor: LogoutExecutor
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
            val user = retrieveUser().first()
            if (user !is User.Found) {
                // If no user is found, we should log the user out of the app.
                handleOnLogoutResult(true)
            } else {
                state = state.copy(
                    userModel = UserModel(
                        avatarUrl = user.domain.avatar.largest?.url ?: "",
                        userName = user.domain.name
                    )
                )
                retrievePreferences().collect { prefModel ->
                    state = state.copy(
                        themeValue = prefModel.theme,
                        defaultHomeValue = prefModel.defaultHomeScreen,
                        defaultSeriesStatusValue = prefModel.defaultSeriesStatus,
                        rateSeriesValue = prefModel.shouldRateSeries,
                        imageQualityValue = prefModel.imageQuality
                    )
                }
            }
        }
    }

    fun execute(action: ViewAction) {
        when (action) {
            ViewAction.OnLogoutClicked -> handleOnLogoutClicked()
            is ViewAction.OnLogoutResult -> handleOnLogoutResult(action.logout)
            ViewAction.ConsumeExecuteLogout -> handleConsumeExecuteLogout()

            ViewAction.OnThemeClicked -> handleOnThemeClicked()
            is ViewAction.OnThemeChanged -> handleOnThemeChanged(action.newTheme)

            ViewAction.OnDefaultHomeScreenClicked -> handleOnDefaultHomeScreenClicked()
            is ViewAction.OnDefaultHomeScreenChanged ->
                handleOnDefaultHomeScreenChanged(action.newHomeScreen)

            ViewAction.OnDefaultSeriesStatusClicked -> handleOnDefaultSeriesStatusClicked()
            is ViewAction.OnDefaultSeriesStatusChanged ->
                handleOnDefaultSeriesStatusChanged(action.newDefaultSeriesStatus)

            ViewAction.OnImageQualityClicked -> handleOnImageQualityClicked()
            is ViewAction.OnImageQualityChanged ->
                handleOnImageQualityChanged(action.newImageQuality)

            is ViewAction.OnRateSeriesChanged -> handleOnRateSeriesChanged(action.newValue)
        }
    }

    private fun handleOnLogoutClicked() {
        state = state.copy(showLogoutDialog = true)
    }

    private fun handleOnLogoutResult(shouldLogout: Boolean) {
        state = state.copy(showLogoutDialog = false)
        if (shouldLogout) {
            viewModelScope.launch {
                logoutExecutor.executeLogout()
                state = state.copy(executeLogout = Unit)
            }
        }
    }

    private fun handleConsumeExecuteLogout() {
        state = state.copy(executeLogout = null)
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

    private fun handleOnImageQualityClicked() {
        state = state.copy(showImageQualityDialog = true)
    }

    private fun handleOnImageQualityChanged(newImageQuality: ImageQuality?) {
        state = state.copy(showImageQualityDialog = false)
        if (newImageQuality != null) {
            viewModelScope.launch {
                updateImageQuality(newImageQuality)
            }
        }
    }

    private fun handleOnRateSeriesChanged(newValue: Boolean) {
        viewModelScope.launch {
            updateRateSeries(newValue)
        }
    }
}
