package com.chesire.nekome.app.login.syncing.core

import com.chesire.nekome.datasource.user.User
import com.chesire.nekome.datasource.user.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RetrieveAvatarUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(): String {
        return userRepository
            .user
            .filterIsInstance<User.Found>()
            .map { user -> user.domain.avatar.largest?.url }
            .firstOrNull()
            ?: ""
    }
}
