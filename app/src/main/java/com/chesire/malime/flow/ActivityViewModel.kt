package com.chesire.malime.flow

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.LogoutHandler
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.kitsu.AuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * [ViewModel] scoped to the [Activity].
 */
class ActivityViewModel @Inject constructor(
    private val authProvider: AuthProvider,
    private val sharedPref: SharedPref,
    private val logoutHandler: LogoutHandler
) : ViewModel() {

    /**
     * Checks against stored data to decide which fragment should be displayed on start.
     */
    val startingFragment: Int
        @IdRes
        get() {
            return if (authProvider.accessToken.isEmpty()) {
                if (sharedPref.isAnalyticsComplete) {
                    R.id.detailsFragment
                } else {
                    R.id.analyticsFragment
                }
            } else {
                R.id.animeFragment
            }
        }

    /**
     * Logs the user out and returns the user back to entering the login details.
     */
    fun logout(callback: () -> Unit) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            logoutHandler.executeLogout()
        }

        Timber.w("Logout complete, firing callback")
        callback()
    }
}
