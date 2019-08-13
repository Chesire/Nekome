package com.chesire.malime.flow

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.IOContext
import com.chesire.malime.LogoutHandler
import com.chesire.malime.R
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.repo.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * [ViewModel] scoped to the [Activity].
 */
class ActivityViewModel @Inject constructor(
    private val authProvider: AuthProvider,
    private val logoutHandler: LogoutHandler,
    @IOContext private val ioContext: CoroutineContext,
    userRepository: UserRepository
) : ViewModel() {
    /**
     * The currently logged in user.
     */
    val user = userRepository.user

    /**
     * Checks against stored data to decide which fragment should be displayed on start.
     */
    val startingFragment: Int
        @IdRes
        get() {
            return if (authProvider.accessToken.isEmpty()) {
                R.id.detailsFragment
            } else {
                R.id.animeFragment
            }
        }

    /**
     * Logs the user out and returns the user back to entering the login details. [callback] is
     * executed after the [LogoutHandler] has finished clearing its data.
     */
    fun logout(callback: () -> Unit) = viewModelScope.launch {
        withContext(ioContext) {
            logoutHandler.executeLogout()
        }

        Timber.w("Logout complete, firing callback")
        callback()
    }
}
