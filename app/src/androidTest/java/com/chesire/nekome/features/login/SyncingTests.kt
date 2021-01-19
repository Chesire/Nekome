package com.chesire.nekome.features.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
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
import com.chesire.nekome.user.api.UserApi
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
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
class SyncingTests {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    val fakeLibrary = mockk<LibraryApi>()

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.logout()
    }

    @Test
    fun successFinishesLoginFlow() {
        coEvery {
            fakeLibrary.retrieveAnime(any())
        } coAnswers {
            Resource.Success(listOf(createLibraryDomain()))
        }
        coEvery {
            fakeLibrary.retrieveManga(any())
        } coAnswers {
            Resource.Success(listOf(createLibraryDomain()))
        }

        launchActivity()
        navigateToSyncing()

        // Nothing to verify, if it passes it was successful
    }

    private fun navigateToSyncing() {
        writeTo(R.id.usernameText, "Username")
        writeTo(R.id.passwordText, "Password")
        // For now use this, will fix it later on
        closeKeyboard()
        clickOn(R.id.loginButton)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    inner class FakeKitsuModule {
        @Provides
        fun providesAuth() = mockk<AuthApi> {
            coEvery {
                login("Username", "Password")
            } coAnswers {
                Resource.Success(Any())
            }
        }

        @Provides
        fun providesLibrary() = fakeLibrary

        @Provides
        fun providesUser() = mockk<UserApi> {
            coEvery {
                getUserDetails()
            } coAnswers {
                Resource.Success(createUserDomain())
            }
        }
    }
}
