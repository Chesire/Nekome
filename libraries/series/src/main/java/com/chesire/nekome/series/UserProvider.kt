package com.chesire.nekome.series

/**
 * Provider for accessing the information for a user.
 */
interface UserProvider {
    /**
     * Executes an async command to acquire the users id.
     */
    suspend fun provideUserId(): Int
}
