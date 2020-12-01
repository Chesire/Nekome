package com.chesire.nekome.flow.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.url.UrlHandler
import com.chesire.nekome.helpers.ToastMatcher.Companion.onToast
import com.chesire.nekome.helpers.clickClickableSpan
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.logout
import com.chesire.nekome.injection.AuthModule
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.injection.UrlModule
import com.chesire.nekome.kitsu.AuthProvider
import com.schibsted.spain.barista.assertion.BaristaErrorAssertions.assertError
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
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
    @JvmField
    val urlHandler = mockk<UrlHandler>()

    val fakeAuth = mockk<AuthApi>()

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.logout()
    }

    @Test
    fun emptyUsernameShowsError() {
        launchActivity()

        writeTo(R.id.usernameText, "")
        writeTo(R.id.passwordText, "Password")
        closeKeyboard()
        clickOn(R.id.loginButton)

        assertError(R.id.usernameLayout, R.string.login_error_empty_username)
    }

    @Test
    fun emptyPasswordShowsError() {
        launchActivity()

        writeTo(R.id.usernameText, "Username")
        writeTo(R.id.passwordText, "")
        closeKeyboard()
        clickOn(R.id.loginButton)

        assertError(R.id.passwordLayout, R.string.login_error_empty_password)
    }

    @Test
    fun invalidCredentialsShowsError() {
        coEvery {
            fakeAuth.login("Username", "Password")
        } coAnswers {
            Resource.Error("Unauthorized error", 401)
        }

        launchActivity()

        writeTo(R.id.usernameText, "Username")
        writeTo(R.id.passwordText, "Password")
        closeKeyboard()
        clickOn(R.id.loginButton)

        onToast(R.string.login_error_credentials).check(matches(isDisplayed()))
    }

    @Test
    fun failureToLoginShowsError() {
        coEvery {
            fakeAuth.login("Username", "Password")
        } coAnswers {
            Resource.Error("Generic error", 0)
        }

        launchActivity()

        writeTo(R.id.usernameText, "Username")
        writeTo(R.id.passwordText, "Password")
        closeKeyboard()
        clickOn(R.id.loginButton)

        onToast(R.string.login_error_generic).check(matches(isDisplayed()))
    }

    @Test
    fun clickingForgotPasswordAttemptsToNavigate() {
        val expectedUrl = R.string.login_forgot_password_url.getResource()
        every { urlHandler.launch(any(), any()) } just Runs

        launchActivity()
        clickOn(R.id.forgotPasswordButton)

        verify { urlHandler.launch(any(), expectedUrl) }
    }

    @Test
    fun clickingSignUpAttemptsToNavigate() {
        val expectedUrl = R.string.login_sign_up_url.getResource()
        every { urlHandler.launch(any(), any()) } just Runs

        launchActivity()
        onView(withId(R.id.signUp))
            .perform(clickClickableSpan(R.string.login_sign_up_link_target))

        verify { urlHandler.launch(any(), expectedUrl) }
    }

    @Module
    @InstallIn(ApplicationComponent::class)
    inner class FakeKitsuModule {
        @Provides
        fun providesAuth() = fakeAuth
    }
}
