package com.chesire.malime.flow.login

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chesire.malime.R
import com.chesire.malime.TestApplication
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.flow.SetupActivity
import com.chesire.malime.flow.OverviewActivity
import com.chesire.malime.helpers.createSeriesModel
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
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
    val loginActivity = ActivityTestRule(SetupActivity::class.java, false, false)

    @Inject
    lateinit var auth: AuthApi
    @Inject
    lateinit var user: UserApi
    @Inject
    lateinit var library: LibraryApi

    @Before
    fun setUp() {
        val app =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication
        app.component.inject(this)

        coEvery {
            auth.login("Username", "Password")
        } coAnswers {
            Resource.Success(Any())
        }
        coEvery {
            user.getUserDetails()
        } coAnswers {
            Resource.Success(
                UserModel(
                    999,
                    "TestUser",
                    ImageModel.empty,
                    ImageModel.empty,
                    Service.Kitsu
                )
            )
        }

        Intents.init()
    }

    @After
    fun tearDown() = Intents.release()

    @Test
    fun failureShowsRetryButton() {
        coEvery {
            library.retrieveAnime(any())
        } coAnswers {
            Resource.Error("Failure")
        }
        coEvery {
            library.retrieveManga(any())
        } coAnswers {
            Resource.Error("Failure")
        }

        loginActivity.launchActivity(null)
        navigateToSyncing()

        assertDisplayed(R.id.fragmentSyncingRetryButton)
    }

    @Test
    fun retryTriesRefreshingSeriesAgain() {
        coEvery {
            library.retrieveAnime(any())
        } returnsMany listOf(
            Resource.Error("Error"),
            Resource.Success(listOf(createSeriesModel()))
        )
        coEvery {
            library.retrieveManga(any())
        } returnsMany listOf(
            Resource.Error("Error"),
            Resource.Success(listOf(createSeriesModel()))
        )

        loginActivity.launchActivity(null)
        navigateToSyncing()
        clickOn(R.id.fragmentSyncingRetryButton)

        intended(hasComponent(OverviewActivity::class.java.name))
    }

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

        loginActivity.launchActivity(null)
        navigateToSyncing()

        intended(hasComponent(OverviewActivity::class.java.name))
    }

    private fun navigateToSyncing() {
        writeTo(R.id.fragmentDetailsUsernameText, "Username")
        writeTo(R.id.fragmentDetailsPasswordText, "Password")
        clickOn(R.id.fragmentDetailsLoginButton)
    }
}
