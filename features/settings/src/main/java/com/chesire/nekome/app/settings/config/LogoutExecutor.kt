package com.chesire.nekome.app.settings.config

/**
 * Interface to populate with a concrete to handle performing log out operations.
 */
interface LogoutExecutor {

    /**
     * Execute log out, clearing anything left over and resetting the application state.
     *
     * This needs to be called using a suspend function so clearing any data such as from databases
     * can be done safely.
     */
    suspend fun executeLogout()
}
