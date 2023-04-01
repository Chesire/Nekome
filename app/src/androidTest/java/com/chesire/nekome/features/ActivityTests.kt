package com.chesire.nekome.features

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginCredentials
import com.chesire.nekome.robots.search.host
import com.chesire.nekome.robots.series.seriesList
import com.chesire.nekome.robots.settings.config
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ActivityTests : UITest() {

    @Inject
    lateinit var applicationPreferences: ApplicationPreferences

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overviewCanStartInAnimeView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Anime) }
        launchActivity()

        seriesList(composeTestRule) {
            validate { isAnimeScreen() }
        }
    }

    @Test
    fun overviewCanStartInMangaView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Manga) }
        launchActivity()

        seriesList(composeTestRule) {
            validate { isMangaScreen() }
        }
    }

    @Test
    fun overviewCanNavigateToAnimeView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Manga) }
        launchActivity()

        activity {
            goToAnime()
        }
        seriesList(composeTestRule) {
            validate { isAnimeScreen() }
        }
    }

    @Test
    fun overviewCanNavigateToMangaView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Anime) }
        launchActivity()

        activity {
            goToManga()
        }
        seriesList(composeTestRule) {
            validate { isMangaScreen() }
        }
    }

    @Test
    fun overviewCanNavigateToSearch() {
        launchActivity()

        activity {
            goToSearch()
        }
        host(composeTestRule) {
            validate { isVisible() }
        }
    }

    @Test
    fun overviewCanNavigateToSettingsView() {
        launchActivity()

        activity {
            goToSettings()
        }
        config(composeTestRule) {
            validate { isVisible() }
        }
    }

    @Test
    fun acceptingLogoutExits() {
        launchActivity()

        activity {
            logout {
                confirm()
            }
        }
        loginCredentials(composeTestRule) {
            validate { isVisible() }
        }
    }

    @Test
    fun decliningLogoutRemains() {
        launchActivity()

        activity {
            logout {
                cancel()
            }
        } validate {
            isVisible()
        }
    }
}
