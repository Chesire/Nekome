package com.chesire.nekome.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repo: AccessTokenRepository,
    private val settings: ApplicationPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState.empty)
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }

    init {
        viewModelScope.launch {
            state = state.copy(
                userLoggedIn = repo.accessToken.isNotEmpty(),
                defaultHomeScreen = parseDefaultHomeScreen(settings.defaultHomeScreen.first())
            )
        }
    }

    private fun parseDefaultHomeScreen(options: HomeScreenOptions): String {
        return when (options) {
            HomeScreenOptions.Anime -> Screen.Anime.route
            HomeScreenOptions.Manga -> Screen.Manga.route
        }
    }
}
