package com.chesire.nekome.features.login

import com.chesire.nekome.UITest
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthFailure
import com.chesire.nekome.robots.login.loginCredentials
import com.github.michaelbull.result.Err
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import javax.inject.Inject
import org.junit.Test

@HiltAndroidTest
class CredentialsTests : UITest() {

    override val startLoggedIn = false

    @Inject
    lateinit var authApi: AuthApi

    @Test
    fun emptyUsernameShowsError() {
        launchActivity()

        loginCredentials(composeTestRule) {
            enterUsername("")
            enterPassword("Password")
            clickLogin()
        } validate {
            isEmptyEmailError()
        }
    }

    @Test
    fun emptyPasswordShowsError() {
        launchActivity()

        loginCredentials(composeTestRule) {
            enterUsername("Username")
            enterPassword("")
            clickLogin()
        } validate {
            isEmptyPasswordError()
        }
    }

    @Test
    fun invalidCredentialsShowsError() {
        coEvery {
            authApi.login("Username", "Password")
        } coAnswers {
            Err(AuthFailure.InvalidCredentials)
        }

        launchActivity()

        loginCredentials(composeTestRule) {
            enterUsername("Username")
            enterPassword("Password")
            clickLogin()
        } validate {
            isInvalidCredentialsError()
        }
    }

    @Test
    fun failureToLoginShowsError() {
        coEvery {
            authApi.login("Username", "Password")
        } coAnswers {
            Err(AuthFailure.BadRequest)
        }

        launchActivity()

        loginCredentials(composeTestRule) {
            enterUsername("Username")
            enterPassword("Password")
            clickLogin()
        } validate {
            isGenericError()
        }
    }
}
