package com.chesire.nekome.app.login.credentials.core

import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VerifyCredentialsUseCase @Inject constructor(private val auth: AccessTokenRepository) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<Unit, VerifyCredentialsFailure> {
        return if (isUsernameInvalid(username)) {
            Err(VerifyCredentialsFailure.UsernameInvalid)
        } else if (isPasswordInvalid(password)) {
            Err(VerifyCredentialsFailure.PasswordInvalid)
        } else {
            withContext(Dispatchers.IO) {
                when (auth.login(username, password)) {
                    AccessTokenResult.CommunicationError -> Err(VerifyCredentialsFailure.NetworkError)
                    AccessTokenResult.InvalidCredentials -> Err(VerifyCredentialsFailure.InvalidCredentials)
                    AccessTokenResult.Success -> Ok(Unit)
                }
            }
        }
    }

    private fun isUsernameInvalid(username: String): Boolean = username.isBlank()

    private fun isPasswordInvalid(password: String): Boolean = password.isBlank()
}

sealed interface VerifyCredentialsFailure {
    object UsernameInvalid : VerifyCredentialsFailure
    object PasswordInvalid : VerifyCredentialsFailure
    object NetworkError : VerifyCredentialsFailure
    object InvalidCredentials : VerifyCredentialsFailure
}
