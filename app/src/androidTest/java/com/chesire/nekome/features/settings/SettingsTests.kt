package com.chesire.nekome.features.settings

import com.chesire.nekome.UITest
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.settings.settings
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class SettingsTests : UITest() {

    @Test
    fun canReachSettings() {
        launchActivity()

        activity {
            goToSettings()
        }
        settings {
            validate { isVisible() }
        }
    }

    @Test
    fun changeDefaultSeriesState() {
        launchActivity()

        activity {
            goToSettings()
        }
        settings {
            defaultSeriesState {
                chooseCurrent()
            } validate {
                currentIsSelected()
            }

            defaultSeriesState {
                chooseCompleted()
            } validate {
                completedIsSelected()
            }

            defaultSeriesState {
                chooseOnHold()
            } validate {
                onHoldIsSelected()
            }

            defaultSeriesState {
                chooseDropped()
            } validate {
                droppedIsSelected()
            }

            defaultSeriesState {
                choosePlanned()
            } validate {
                plannedIsSelected()
            }
        }
    }

    @Test
    fun changeDefaultHomeScreenState() {
        launchActivity()

        activity {
            goToSettings()
        }
        settings {
            defaultHomeScreen {
                chooseAnime()
            } validate {
                animeIsSelected()
            }

            defaultHomeScreen {
                chooseManga()
            } validate {
                mangaIsSelected()
            }
        }
    }

    @Test
    fun changeThemeSetting() {
        launchActivity()

        activity {
            goToSettings()
        }
        settings {
            changeTheme {
                chooseDark()
                activity { goToSettings() }
            } validate {
                darkIsSelected()
            }

            changeTheme {
                chooseLight()
                activity { goToSettings() }
            } validate {
                lightIsSelected()
            }

            changeTheme {
                chooseSystem()
            } validate {
                systemIsSelected()
            }
        }
    }

    @Test
    fun openingGitHubHasNoIssue() {
        launchActivity()

        activity {
            goToSettings()
        }
        settings {
            openGitHub()
        }
    }

    @Test
    fun openOSS() {
        launchActivity()

        activity {
            goToSettings()
        }
        settings {
            goToLicenses()
        }
    }
}
