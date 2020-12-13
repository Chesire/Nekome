package com.chesire.nekome.library

/**
 * Provider for accessing the information for a user.
 */
interface UserProvider {
    /**
     * Executes an async command to acquire the users id.
     */
    suspend fun provideUserId(): UserIdResult

    /**
     * Represents different result states that [provideUserId] can provide.
     */
    sealed class UserIdResult {
        /**
         * Success state containing the user id.
         */
        data class Success(val id: Int) : UserIdResult()

        /**
         * Failure state when id could not be retrieved.
         */
        object Failure : UserIdResult()
    }
}
