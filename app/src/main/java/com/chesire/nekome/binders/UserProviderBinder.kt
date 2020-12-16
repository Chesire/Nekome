package com.chesire.nekome.binders

import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.library.UserProvider
import javax.inject.Inject

/**
 * Provides a concrete class to bind the [UserProvider] interface.
 */
class UserProviderBinder @Inject constructor(
    private val userRepository: UserRepository
) : UserProvider {

    override suspend fun provideUserId(): UserProvider.UserIdResult {
        val id = userRepository.retrieveUserId()
        return if (id == null) {
            UserProvider.UserIdResult.Failure
        } else {
            UserProvider.UserIdResult.Success(id)
        }
    }
}
