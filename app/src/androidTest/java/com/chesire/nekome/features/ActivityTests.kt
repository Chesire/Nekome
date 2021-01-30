package com.chesire.nekome.features

import com.chesire.nekome.UITest
import com.chesire.nekome.core.flags.HomeScreenOptions
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.login.loginDetails
import com.chesire.nekome.robots.search.search
import com.chesire.nekome.robots.series.seriesList
import com.chesire.nekome.robots.settings.settings
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class ActivityTests : UITest() {

    @Inject
    lateinit var applicationSettings: ApplicationSettings

    @Test
    fun overviewCanStartInAnimeView() {
        applicationSettings.defaultHomeScreen = HomeScreenOptions.Anime
        launchActivity()

        seriesList {
            validate { isAnimeScreen() }
        }
    }

    @Test
    fun overviewCanStartInMangaView() {
        applicationSettings.defaultHomeScreen = HomeScreenOptions.Manga
        launchActivity()

        seriesList {
            validate { isMangaScreen() }
        }
    }

    @Test
    fun overviewCanNavigateToAnimeView() {
        applicationSettings.defaultHomeScreen = HomeScreenOptions.Manga
        launchActivity()

        activity {
            goToAnime()
        }
        seriesList {
            validate { isAnimeScreen() }
        }
    }

    @Test
    fun overviewCanNavigateToMangaView() {
        applicationSettings.defaultHomeScreen = HomeScreenOptions.Anime
        launchActivity()

        activity {
            goToManga()
        }
        seriesList {
            validate { isMangaScreen() }
        }
    }

    @Test
    fun overviewCanNavigateToSearch() {
        launchActivity()

        activity {
            goToSearch()
        }
        search {
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
        loginDetails {
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
