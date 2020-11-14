package com.chesire.nekome.flow.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.logout
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.injection.KitsuModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
import com.chesire.nekome.server.api.LibraryApi
import com.chesire.nekome.server.api.SearchApi
import com.chesire.nekome.server.api.TrendingApi
import com.chesire.nekome.server.api.UserApi
import com.chesire.nekome.testing.createSeriesModel
import com.chesire.nekome.testing.createUserModel
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
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
@UninstallModules(DatabaseModule::class, KitsuModule::class)
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
            Resource.Success(listOf(createSeriesModel()))
        }
        coEvery {
            fakeLibrary.retrieveManga(any())
        } coAnswers {
            Resource.Success(listOf(createSeriesModel()))
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
    @InstallIn(ApplicationComponent::class)
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
        fun providesSearch() = mockk<SearchApi>()

        @Provides
        fun providesTrending() = mockk<TrendingApi>()

        @Provides
        fun providesUser() = mockk<UserApi> {
            coEvery {
                getUserDetails()
            } coAnswers {
                Resource.Success(createUserModel())
            }
        }
    }
}

