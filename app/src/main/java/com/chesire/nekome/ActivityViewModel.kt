package com.chesire.nekome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.chesire.nekome.core.IOContext
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.user.UserRepository
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

/**
 * [ViewModel] scoped to the [Activity].
 */
@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repo: AccessTokenRepository,
    private val logoutHandler: LogoutHandler,
    private val settings: ApplicationPreferences,
    @IOContext private val ioContext: CoroutineContext,
    userRepository: UserRepository
) : ViewModel() {

    private val _navigation = LiveEvent<NavDirections>()
    val navigation: LiveData<NavDirections> = _navigation

    private val _snackBar = LiveEvent<Unit>()
    val snackBar: LiveData<Unit> = _snackBar

    // init {
    //     if (!userLoggedIn) {
    //         navigateTo(OverviewNavGraphDirections.globalToLoginFlow())
    //     } else {
    //         navigateToDefaultHome()
    //     }
    // }

    /**
     * The currently logged in user.
     */
    val user = userRepository.user.asLiveData()

    /**
     * Checks if the user is currently logged in.
     */
    // val userLoggedIn: Boolean
    //     get() = repo.accessToken.isNotEmpty()

    /**
     * Logs the user out and returns the user back to entering the login details.
     */
    fun logout(isFailure: Boolean = false) = viewModelScope.launch(ioContext) {
        logoutHandler.executeLogout()

        //navigateTo(OverviewNavGraphDirections.globalToLoginFlow())

        if (isFailure) {
            _snackBar.postValue(Unit)
        }
    }

    // fun navigateToDefaultHome() {
    //     viewModelScope.launch {
    //         if (settings.defaultHomeScreen.first() == HomeScreenOptions.Anime) {
    //             navigateTo(OverviewNavGraphDirections.globalToAnimeFragment())
    //         } else {
    //             navigateTo(OverviewNavGraphDirections.globalToMangaFragment())
    //         }
    //     }
    // }

    // private fun navigateTo(destination: NavDirections) {
    //     _navigation.postValue(destination)
    // }
}
