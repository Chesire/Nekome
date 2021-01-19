package com.chesire.nekome.features.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.url.UrlHandler
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.logout
import com.chesire.nekome.injection.AuthModule
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.injection.UrlModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.robots.login.loginDetails
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class, AuthModule::class, UrlModule::class)
@RunWith(AndroidJUnit4::class)
class DetailsTests {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @BindValue
    val urlHandler = mockk<UrlHandler>()

    @BindValue
    val fakeAuth = mockk<AuthApi>()

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.logout()
    }

    @Test
    fun emptyUsernameShowsError() {
        launchActivity()

        loginDetails {
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

        loginDetails {
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
            fakeAuth.login("Username", "Password")
        } coAnswers {
            Resource.Error.invalidAuth()
        }

        launchActivity()

        loginDetails {
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
            fakeAuth.login("Username", "Password")
        } coAnswers {
            Resource.Error("Generic error", Resource.Error.GenericError)
        }

        launchActivity()

        loginDetails {
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

        loginDetails {
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

        loginDetails {
            clickSignUp()
        } validate {
            verify { urlHandler.launch(any(), expectedUrl) }
        }
    }
}
