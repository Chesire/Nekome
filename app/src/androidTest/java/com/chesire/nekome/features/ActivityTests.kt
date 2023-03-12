package com.chesire.nekome.features

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.core.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginCredentials
import com.chesire.nekome.robots.search.host
import com.chesire.nekome.robots.series.seriesList
import com.chesire.nekome.robots.settings.settings
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
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
        applicationPreferences.defaultHomeScreen = HomeScreenOptions.Anime
        launchActivity()

        seriesList(composeTestRule) {
            validate { isAnimeScreen() }
        }
    }

    @Test
    fun overviewCanStartInMangaView() {
        applicationPreferences.defaultHomeScreen = HomeScreenOptions.Manga
        launchActivity()

        seriesList(composeTestRule) {
            validate { isMangaScreen() }
        }
    }

    @Test
    fun overviewCanNavigateToAnimeView() {
        applicationPreferences.defaultHomeScreen = HomeScreenOptions.Manga
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
        applicationPreferences.defaultHomeScreen = HomeScreenOptions.Anime
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
        settings {
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
