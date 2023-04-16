package com.chesire.nekome.features.login

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.datasource.auth.remote.AuthApi
import com.chesire.nekome.datasource.auth.remote.AuthFailure
import com.chesire.nekome.datasource.series.remote.SeriesApi
import com.chesire.nekome.datasource.user.remote.UserApi
import com.chesire.nekome.helpers.creation.createSeriesDomain
import com.chesire.nekome.helpers.creation.createUserDomain
import com.chesire.nekome.injection.AuthModule
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.injection.UserModule
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginCredentials
import com.chesire.nekome.robots.login.loginSyncing
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(
    AuthModule::class,
    LibraryModule::class,
    UserModule::class
)
class LoginFlowTests : UITest() {

    override val startLoggedIn = false

    @BindValue
    val authApi = mockk<AuthApi> {
        coEvery {
            login("Username", "Password")
        } coAnswers {
            AuthFailure.Success("accessToken", "refreshToken")
        }
    }

    @BindValue
    val seriesApi = mockk<SeriesApi> {
        coEvery {
            retrieveAnime(any())
        } coAnswers {
            Resource.Success(listOf(createSeriesDomain()))
        }
        coEvery {
            retrieveManga(any())
        } coAnswers {
            Resource.Success(listOf(createSeriesDomain()))
        }
    }

    @BindValue
    val userApi = mockk<UserApi> {
        coEvery {
            getUserDetails()
        } coAnswers {
            Resource.Success(createUserDomain())
        }
    }

    @get:Rule
    val composeTestRule = createComposeRule()

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
