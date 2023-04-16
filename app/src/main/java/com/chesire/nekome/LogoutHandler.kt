package com.chesire.nekome

import com.chesire.nekome.app.settings.config.LogoutExecutor
import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Handles clearing out resources for when a log out occurs.
 */
class LogoutHandler @Inject constructor(
    private val repo: AccessTokenRepository,
    private val db: RoomDB
) : LogoutExecutor {

    override suspend fun executeLogout() {
        withContext(Dispatchers.IO) {
            Timber.d("Clearing database tables")
            db.clearAllTables()
        }
        Timber.d("Clearing auth")
        repo.clear()
    }
}
