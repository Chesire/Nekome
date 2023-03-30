package com.chesire.nekome.features.settings

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.settings.config
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SettingsTests : UITest() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun canReachSettings() {
        launchActivity()

        activity {
            goToSettings()
        }
        config(composeTestRule) {
            validate { isVisible() }
        }
    }

    @Test
    fun changeDefaultSeriesState() {
        launchActivity()

        activity {
            goToSettings()
        }
        config(composeTestRule) {
            clickDefaultSeriesState()

            defaultSeriesState {
                validate {
                    isLoadedCorrectly()
                }
            }

            defaultSeriesState {
                chooseCurrent()
                validate {
                    currentIsSelected()
                }
                confirm()
            }

            clickDefaultSeriesState()
            defaultSeriesState {
                chooseCompleted()
                validate {
                    completedIsSelected()
                }
                confirm()
            }

            clickDefaultSeriesState()
            defaultSeriesState {
                chooseOnHold()
                validate {
                    onHoldIsSelected()
                }
                confirm()
            }

            clickDefaultSeriesState()
            defaultSeriesState {
                chooseDropped()
                validate {
                    droppedIsSelected()
                }
                confirm()
            }

            clickDefaultSeriesState()
            defaultSeriesState {
                choosePlanned()
                validate {
                    plannedIsSelected()
                }
                confirm()
            }
        }
    }

    @Test
    fun changeDefaultHomeScreenState() {
        launchActivity()

        activity {
            goToSettings()
        }
        config(composeTestRule) {
            clickDefaultHomeScreen()

            defaultHomeScreen {
                validate {
                    isLoadedCorrectly()
                }
            }

            defaultHomeScreen {
                chooseAnime()
                validate {
                    animeIsSelected()
                }
                confirm()
            }

            clickDefaultHomeScreen()
            defaultHomeScreen {
                chooseManga()
                validate {
                    mangaIsSelected()
                }
                confirm()
            }
        }
    }

    @Test
    fun changeThemeSetting() {
        launchActivity()

        activity {
            goToSettings()
        }
        config(composeTestRule) {
            clickTheme()

            changeTheme {
                validate {
                    isLoadedCorrectly()
                }
            }

            changeTheme {
                chooseDark()
                validate {
                    darkIsSelected()
                }
                confirm()
            }

            clickTheme()
            changeTheme {
                chooseLight()
                validate {
                    lightIsSelected()
                }
                confirm()
            }

            clickTheme()
            changeTheme {
                chooseSystem()
                validate {
                    systemIsSelected()
                }
                confirm()
            }
        }
    }

    @Test
    fun changeRateOnCompletionSetting() {
        launchActivity()

        activity {
            goToSettings()
        }
        config(composeTestRule) {
            changeRateOnComplete()
        }
    }

    @Test
    fun openOSS() {
        launchActivity()

        activity {
            goToSettings()
        }
        config(composeTestRule) {
            goToLicenses()
        }
    }
}
