package com.chesire.nekome.binders

import com.chesire.nekome.datasource.series.UserProvider
import com.chesire.nekome.datasource.user.UserRepository
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
