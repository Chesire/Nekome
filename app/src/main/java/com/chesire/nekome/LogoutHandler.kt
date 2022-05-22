package com.chesire.nekome

import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import javax.inject.Inject
import timber.log.Timber

/**
 * Handles clearing out resources for when a log out occurs.
 */
class LogoutHandler @Inject constructor(
    private val repo: AccessTokenRepository,
    private val db: RoomDB
) {

    /**
     * Executes log out, clearing anything left over and resetting the application state.
     * This should be called in a background handler so that the database can safely be cleared.
     */
    fun executeLogout() {
        Timber.d("Clearing database tables")
        db.clearAllTables()
        Timber.d("Clearing auth")
        repo.clear()
    }
}
