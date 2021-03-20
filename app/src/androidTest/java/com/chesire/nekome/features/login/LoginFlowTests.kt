package com.chesire.nekome.features.login

import com.chesire.nekome.UITest
import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.user.remote.UserApi
import com.chesire.nekome.helpers.creation.createLibraryDomain
import com.chesire.nekome.helpers.creation.createUserDomain
import com.chesire.nekome.injection.AuthModule
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.injection.UserModule
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginDetails
import com.chesire.nekome.robots.login.loginSyncing
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
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
            Resource.Success(Any())
        }
    }

    @BindValue
    val libraryApi = mockk<LibraryApi> {
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
    val userApi = mockk<UserApi> {
        coEvery {
            getUserDetails()
        } coAnswers {
            Resource.Success(createUserDomain())
        }
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
            // Skipped over, too fast to test
        }
        activity {
            validate { isVisible() }
        }
    }
}
