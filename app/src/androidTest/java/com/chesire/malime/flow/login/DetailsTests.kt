package com.chesire.malime.flow.login

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.R
import com.chesire.malime.flow.Activity
import com.chesire.malime.helpers.ToastMatcher.Companion.onToast
import com.chesire.malime.helpers.injector
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.AuthApi
import com.chesire.malime.server.api.LibraryApi
import com.chesire.malime.server.api.UserApi
import com.schibsted.spain.barista.assertion.BaristaErrorAssertions.assertError
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import io.mockk.coEvery
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

        writeTo(R.id.detailsUsernameText, "")
        writeTo(R.id.detailsPasswordText, "Password")
        closeKeyboard()
        clickOn(R.id.detailsLoginButton)

        assertError(R.id.detailsUsernameLayout, R.string.login_error_empty_username)
    }

    @Test
    fun emptyPasswordShowsError() {
        activity.launchActivity(null)

        writeTo(R.id.detailsUsernameText, "Username")
        writeTo(R.id.detailsPasswordText, "")
        closeKeyboard()
        clickOn(R.id.detailsLoginButton)

        assertError(R.id.detailsPasswordLayout, R.string.login_error_empty_password)
    }

    @Test
    fun invalidCredentialsShowsError() {
        coEvery {
            auth.login("Username", "Password")
        } coAnswers {
            Resource.Error("Unauthorized error", 401)
        }

        activity.launchActivity(null)

        writeTo(R.id.detailsUsernameText, "Username")
        writeTo(R.id.detailsPasswordText, "Password")
        closeKeyboard()
        clickOn(R.id.detailsLoginButton)

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

        writeTo(R.id.detailsUsernameText, "Username")
        writeTo(R.id.detailsPasswordText, "Password")
        closeKeyboard()
        clickOn(R.id.detailsLoginButton)

        onToast(R.string.login_error_generic).check(matches(isDisplayed()))
    }
}
