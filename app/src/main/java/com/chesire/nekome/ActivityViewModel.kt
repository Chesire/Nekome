package com.chesire.nekome

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.chesire.nekome.core.IOContext
import com.chesire.nekome.core.flags.HomeScreenOptions
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.user.UserRepository
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * [ViewModel] scoped to the [Activity].
 */
@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repo: AccessTokenRepository,
    private val logoutHandler: LogoutHandler,
    private val settings: ApplicationSettings,
    @IOContext private val ioContext: CoroutineContext,
    userRepository: UserRepository
) : ViewModel() {

    val navigation = LiveEvent<NavDirections>()
    val snackBar = LiveEvent<Unit>()

    init {
        if (!userLoggedIn) {
            navigateTo(OverviewNavGraphDirections.globalToDetailsFragment())
        } else {
            navigateToDefaultHome()
        }
    }

    /**
     * The currently logged in user.
     */
    val user = userRepository.user.asLiveData()

    /**
     * Checks if the user is currently logged in.
     */
    val userLoggedIn: Boolean
        get() = repo.accessToken.isNotEmpty()

    /**
     * Logs the user out and returns the user back to entering the login details.
     */
    fun logout(isFailure: Boolean = false) = viewModelScope.launch(ioContext) {
        logoutHandler.executeLogout()

        navigateTo(OverviewNavGraphDirections.globalToDetailsFragment())

        if (isFailure) {
            snackBar.postValue(Unit)
        }
    }

    fun navigateToDefaultHome() {
        if (settings.defaultHomeScreen == HomeScreenOptions.Anime) {
            navigateTo(OverviewNavGraphDirections.globalToAnimeFragment())
        } else {
            navigateTo(OverviewNavGraphDirections.globalToMangaFragment())
        }
    }

    private fun navigateTo(destination: NavDirections) {
        navigation.postValue(destination)
    }
}
