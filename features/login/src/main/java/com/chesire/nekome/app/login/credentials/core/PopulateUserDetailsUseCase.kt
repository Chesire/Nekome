package com.chesire.nekome.app.login.credentials.core

import com.chesire.nekome.datasource.user.UserRepository
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PopulateUserDetailsUseCase @Inject constructor(private val user: UserRepository) {

    suspend operator fun invoke(): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            user.refreshUser()
        }
    }
}
