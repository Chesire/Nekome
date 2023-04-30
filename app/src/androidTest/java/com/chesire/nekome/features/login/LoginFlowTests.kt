package com.chesire.nekome.features.login

import com.chesire.nekome.UITest
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthDomain
import com.chesire.nekome.datasource.series.remote.SeriesApi
import com.chesire.nekome.datasource.user.remote.UserApi
import com.chesire.nekome.helpers.creation.createSeriesDomain
import com.chesire.nekome.helpers.creation.createUserDomain
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.injection.UserModule
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginCredentials
import com.chesire.nekome.robots.login.loginSyncing
import com.github.michaelbull.result.Ok
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Inject
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
@UninstallModules(
    LibraryModule::class,
    UserModule::class
)
class LoginFlowTests : UITest() {

    override val startLoggedIn = false

    @Inject
    lateinit var authApi: AuthApi

    @BindValue
    val seriesApi = mockk<SeriesApi> {
        coEvery {
            retrieveAnime(any())
        } coAnswers {
            Ok(listOf(createSeriesDomain()))
        }
        coEvery {
            retrieveManga(any())
        } coAnswers {
            Ok(listOf(createSeriesDomain()))
        }
    }

    @BindValue
    val userApi = mockk<UserApi> {
        coEvery {
            getUserDetails()
        } coAnswers {
            Ok(createUserDomain())
        }
    }

    @Before
    fun setup() {
        coEvery {
            authApi.login("Username", "Password")
        } coAnswers {
            Ok(AuthDomain("accessToken", "refreshToken"))
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
