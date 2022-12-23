package com.chesire.nekome.features.login

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.R
import com.chesire.nekome.UITest
import com.chesire.nekome.core.url.UrlHandler
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthResult
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.injection.AuthModule
import com.chesire.nekome.injection.UrlModule
import com.chesire.nekome.robots.login.loginCredentials
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(
    AuthModule::class,
    UrlModule::class
)
class CredentialsTests : UITest() {

    override val startLoggedIn = false

    @BindValue
    val urlHandler = mockk<UrlHandler>()

    @BindValue
    val authApi = mockk<AuthApi>()

    @get:Rule
    val composeTestRule = createComposeRule()

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
            AuthResult.InvalidCredentials
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
            AuthResult.BadRequest
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

    @Test
    fun clickingForgotPasswordAttemptsToNavigate() {
        val expectedUrl = R.string.login_forgot_password_url.getResource()
        every { urlHandler.launch(any(), any()) } just Runs

        launchActivity()

        loginCredentials(composeTestRule) {
            clickForgotPassword()
        } validate {
            verify { urlHandler.launch(any(), expectedUrl) }
        }
    }

    @Test
    fun clickingSignUpAttemptsToNavigate() {
        val expectedUrl = R.string.login_sign_up_url.getResource()
        every { urlHandler.launch(any(), any()) } just Runs

        launchActivity()

        loginCredentials(composeTestRule) {
            clickSignUp()
        } validate {
            verify { urlHandler.launch(any(), expectedUrl) }
        }
    }
}
