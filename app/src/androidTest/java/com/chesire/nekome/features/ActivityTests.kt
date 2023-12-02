package com.chesire.nekome.features

import com.chesire.nekome.UITest
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.robots.activity
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Test

@HiltAndroidTest
class ActivityTests : UITest() {

    @Test
    fun overviewCanStartInAnimeView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Anime) }
        launchActivity()

        activity(composeTestRule) {
            validate {
                isOnAnimeScreen()
            }
        }
    }

    @Test
    fun overviewCanStartInMangaView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Manga) }
        launchActivity()

        activity(composeTestRule) {
            validate {
                isOnMangaScreen()
            }
        }
    }

    @Test
    fun overviewCanNavigateToAnimeView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Manga) }
        launchActivity()

        activity(composeTestRule) {
            goToAnime()
            validate {
                isOnAnimeScreen()
            }
        }
    }

    @Test
    fun overviewCanNavigateToMangaView() {
        runBlocking { applicationPreferences.updateDefaultHomeScreen(HomeScreenOptions.Anime) }
        launchActivity()

        activity(composeTestRule) {
            goToManga()
            validate {
                isOnMangaScreen()
            }
        }
    }

    @Test
    fun overviewCanNavigateToSettingsView() {
        launchActivity()

        activity(composeTestRule) {
            goToSettings()
            validate {
                isOnSettingsScreen()
            }
        }
    }
}
