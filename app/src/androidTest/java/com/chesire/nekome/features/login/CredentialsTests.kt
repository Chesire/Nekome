package com.chesire.nekome.features.login

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthFailure
import com.chesire.nekome.injection.AuthModule
import com.chesire.nekome.robots.login.loginCredentials
import com.github.michaelbull.result.Err
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AuthModule::class)
class CredentialsTests : UITest() {

    override val startLoggedIn = false

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
