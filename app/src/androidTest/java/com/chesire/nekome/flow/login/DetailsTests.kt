package com.chesire.nekome.flow.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.nekome.Activity
import com.chesire.nekome.R
import com.chesire.nekome.core.url.UrlHandler
import com.chesire.nekome.helpers.ToastMatcher.Companion.onToast
import com.chesire.nekome.helpers.clickClickableSpan
import com.chesire.nekome.helpers.getResource
import com.chesire.nekome.helpers.injector
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
import com.chesire.nekome.server.api.LibraryApi
import com.chesire.nekome.server.api.UserApi
import com.schibsted.spain.barista.assertion.BaristaErrorAssertions.assertError
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class DetailsTests {
    @get:Rule
    val activity = ActivityTestRule(Activity::class.java, false, false)

    @get:Rule
    val clearPreferencesRule = ClearPreferencesRule()

    @Inject
    lateinit var auth: AuthApi

    @Inject
    lateinit var user: UserApi

    @Inject
    lateinit var library: LibraryApi

    @Inject
    lateinit var authProvider: AuthProvider

    @Inject
    lateinit var urlHandler: UrlHandler

    @Before
    fun setUp() {
        injector.inject(this)

        authProvider.accessToken = ""

        coEvery {
            library.retrieveAnime(any())
        } coAnswers {
            Resource.Error("No need in this test file")
        }
        coEvery {
            library.retrieveManga(any())
        } coAnswers {
            Resource.Error("No need in this test file")
        }
    }

    @Test
    fun emptyUsernameShowsError() {
        activity.launchActivity(null)

        writeTo(R.id.usernameText, "")
        writeTo(R.id.passwordText, "Password")
        closeKeyboard()
        clickOn(R.id.loginButton)

        assertError(R.id.usernameLayout, R.string.login_error_empty_username)
    }

    @Test
    fun emptyPasswordShowsError() {
        activity.launchActivity(null)

        writeTo(R.id.usernameText, "Username")
        writeTo(R.id.passwordText, "")
        closeKeyboard()
        clickOn(R.id.loginButton)

        assertError(R.id.passwordLayout, R.string.login_error_empty_password)
    }

    @Test
    fun invalidCredentialsShowsError() {
        coEvery {
            auth.login("Username", "Password")
        } coAnswers {
            Resource.Error("Unauthorized error", 401)
        }

        activity.launchActivity(null)

        writeTo(R.id.usernameText, "Username")
        writeTo(R.id.passwordText, "Password")
        closeKeyboard()
        clickOn(R.id.loginButton)

        onToast(R.string.login_error_credentials).check(matches(isDisplayed()))
    }

    @Test
    fun failureToLoginShowsError() {
        coEvery {
            auth.login("Username", "Password")
        } coAnswers {
            Resource.Error("Generic error", 0)
        }

        activity.launchActivity(null)

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

        activity.launchActivity(null)
        clickOn(R.id.forgotPasswordButton)

        verify { urlHandler.launch(any(), expectedUrl) }
    }

    @Test
    fun clickingSignUpAttemptsToNavigate() {
        val expectedUrl = R.string.login_sign_up_url.getResource()
        every { urlHandler.launch(any(), any()) } just Runs

        activity.launchActivity(null)
        onView(withId(R.id.signUp))
            .perform(clickClickableSpan(R.string.login_sign_up_link_target))

        verify { urlHandler.launch(any(), expectedUrl) }
    }
}
