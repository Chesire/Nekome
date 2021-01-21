package com.chesire.nekome.features.settings

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.login
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.robots.activity
import com.chesire.nekome.robots.settings.settings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
class SettingsTests {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @Before
    fun setUp() {
        hilt.inject()
        authProvider.login()
    }

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
    fun openOSS() {
        launchActivity()

        activity {
            goToSettings()
        }
        settings {
            clickLicenses()
        }
    }
}
