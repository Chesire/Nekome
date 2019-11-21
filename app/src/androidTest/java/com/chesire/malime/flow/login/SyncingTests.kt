package com.chesire.malime.flow.login

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.R
import com.chesire.malime.flow.Activity
import com.chesire.malime.helpers.injector
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.AuthApi
import com.chesire.malime.server.api.LibraryApi
import com.chesire.malime.server.api.UserApi
import com.chesire.malime.testing.createSeriesModel
import com.chesire.malime.testing.createUserModel
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import io.mockk.coEvery
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SyncingTests {
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
            auth.login("Username", "Password")
        } coAnswers {
            Resource.Success(Any())
        }
        coEvery {
            user.getUserDetails()
        } coAnswers {
            Resource.Success(createUserModel())
        }

        Intents.init()
    }

    @After
    fun tearDown() = Intents.release()

    @Test
    fun successFinishesLoginFlow() {
        coEvery {
            library.retrieveAnime(any())
        } coAnswers {
            Resource.Success(listOf(createSeriesModel()))
        }
        coEvery {
            library.retrieveManga(any())
        } coAnswers {
            Resource.Success(listOf(createSeriesModel()))
        }

        activity.launchActivity(null)
        navigateToSyncing()

        intended(hasComponent(Activity::class.java.name))
    }

    private fun navigateToSyncing() {
        writeTo(R.id.detailsUsernameText, "Username")
        writeTo(R.id.detailsPasswordText, "Password")
        // For now use this, will fix it later on
        closeKeyboard()
        clickOn(R.id.detailsLoginButton)
    }
}
