package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.datasource.user.User
import com.chesire.nekome.datasource.user.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class RetrieveUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    operator fun invoke(): Flow<User> = userRepository.user
}
