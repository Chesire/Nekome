package com.chesire.nekome.datasource.user

/**
 * Possible results for accessing the user object from the [UserRepository].
 */
sealed class User {

    /**
     * User has been found with a valid model.
     */
    data class Found(val domain: UserDomain) : User()

    /**
     * No user has been found.
     */
    object NotFound : User()
}
