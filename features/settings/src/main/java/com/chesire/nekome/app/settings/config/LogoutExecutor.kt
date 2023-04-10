package com.chesire.nekome.app.settings.config

/**
 * Interface to populate with a concrete to handle performing log out operations.
 */
interface LogoutExecutor {

    suspend fun executeLogout()
}
