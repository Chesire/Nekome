package com.chesire.malime

import com.chesire.malime.db.RoomDB
import com.chesire.malime.kitsu.AuthProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * Handles clearing out resources for when a log out occurs.
 */
class LogoutHandler @Inject constructor(
    private val authProvider: AuthProvider,
    private val db: RoomDB
) {
    /**
     * Executes log out, clearing anything left over and resetting the application state.
     */
    fun executeLogout() {
        Timber.d("Executing log out")

        db.clearAllTables()
        authProvider.clearAuth()
    }
}
