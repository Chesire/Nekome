package com.chesire.nekome.features.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.chesire.nekome.helpers.creation.createLibraryDomain
import com.chesire.nekome.helpers.creation.createUserDomain
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.logout
import com.chesire.nekome.injection.AuthModule
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.injection.UserModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginDetails
import com.chesire.nekome.robots.login.loginSyncing
import com.chesire.nekome.user.api.UserApi
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(
    AuthModule::class,
    DatabaseModule::class,
    LibraryModule::class,
    UserModule::class
)
@RunWith(AndroidJUnit4::class)
class LoginFlowTests {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @BindValue
    val fakeAuth = mockk<AuthApi> {
        coEvery {
            login("Username", "Password")
        } coAnswers {
            Resource.Success(Any())
        }
    }

    @BindValue
    val fakeLibrary = mockk<LibraryApi> {
        coEvery {
            retrieveAnime(any())
        } coAnswers {
            Resource.Success(listOf(createLibraryDomain()))
        }
        coEvery {
            retrieveManga(any())
        } coAnswers {
            Resource.Success(listOf(createLibraryDomain()))
        }
    }

    @BindValue
    val fakeUser = mockk<UserApi> {
        coEvery {
            getUserDetails()
        } coAnswers {
            Resource.Success(createUserDomain())
        }
    }

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.logout()
    }

    @Test
    fun navigateThroughLoginFlow() {
        launchActivity()

        loginDetails {
            enterUsername("Username")
            enterPassword("Password")
            clickLogin()
        }
        loginSyncing {
            // Skipped over too fast to test
        }
        activity {
            validate { isVisible() }
        }
    }
}
