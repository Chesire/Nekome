package com.chesire.nekome.binders

import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.series.UserProvider
import javax.inject.Inject

/**
 * Provides a concrete class to bind the [UserProvider] interface.
 */
class UserProviderBinder @Inject constructor(
    private val userRepository: UserRepository
) : UserProvider {

    override suspend fun provideUserId() = requireNotNull(userRepository.retrieveUserId()) {
        "UserID should not be null in the database"
    }
}
