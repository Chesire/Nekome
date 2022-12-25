package com.chesire.nekome.app.login.credentials.core

import com.chesire.nekome.datasource.auth.AccessTokenRepository
import javax.inject.Inject

class ClearCredentialsUseCase @Inject constructor(private val auth: AccessTokenRepository) {

    operator fun invoke() = auth.clear()
}
