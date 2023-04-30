package com.chesire.nekome.features.login

import com.chesire.nekome.UITest
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthDomain
import com.chesire.nekome.datasource.series.remote.SeriesApi
import com.chesire.nekome.datasource.user.remote.UserApi
import com.chesire.nekome.helpers.creation.createSeriesDomain
import com.chesire.nekome.helpers.creation.createUserDomain
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginCredentials
import com.chesire.nekome.robots.login.loginSyncing
import com.github.michaelbull.result.Ok
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import javax.inject.Inject
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class LoginFlowTests : UITest() {

    override val startLoggedIn = false

    @Inject
    lateinit var authApi: AuthApi

    @Inject
    lateinit var seriesApi: SeriesApi

    @Inject
    lateinit var userApi: UserApi

    @Before
    fun setup() {
        coEvery {
            authApi.login("Username", "Password")
        } coAnswers {
            Ok(AuthDomain("accessToken", "refreshToken"))
        }

        coEvery {
            seriesApi.retrieveAnime(any())
        } coAnswers {
            Ok(listOf(createSeriesDomain()))
        }
        coEvery {
            seriesApi.retrieveManga(any())
        } coAnswers {
            Ok(listOf(createSeriesDomain()))
        }

        coEvery {
            userApi.getUserDetails()
        } coAnswers {
            Ok(createUserDomain())
        }
    }

    @Test
    fun navigateThroughLoginFlow() {
        launchActivity()

        loginCredentials(composeTestRule) {
            enterUsername("Username")
            enterPassword("Password")
            clickLogin()
        }
        loginSyncing(composeTestRule) {
            // Skipped over, too fast to test
        }
        activity(composeTestRule) {
            validate { isVisible() }
        }
    }
}
