package com.chesire.nekome.features.series

import com.chesire.nekome.UITest
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.robots.series.seriesList
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class FilterTests : UITest() {

    @Test
    fun filterDialogDisplaysWithAllOptions() {
        launchActivity()

        seriesList {
            filterSeries {
                open()
            } validate {
                isVisible()
                isLoadedCorrectly()
            }
        }
    }

    @Test
    fun filterDialogHasCorrectDefaultChoices() {
        launchActivity()

        seriesList {
            filterSeries {
                open()
            } validate {
                isInDefaultState()
            }
        }
    }

    @Test
    fun filterDialogRetainsLastChoices() {
        launchActivity()

        seriesList {
            filterSeries {
                open()
                clickOnHold()
                clickPlanned()
                confirm()
                open()
            } validate {
                isCurrentChecked()
                isCompletedNotChecked()
                isOnHoldChecked()
                isDroppedNotChecked()
                isPlannedChecked()
            }
        }
    }

    @Test
    fun filterDialogCancelsOnCancelHit() {
        launchActivity()

        seriesList {
            filterSeries {
                open()
                clickCompleted()
                clickDropped()
                cancel()
                open()
            } validate {
                isCurrentChecked()
                isCompletedNotChecked()
                isOnHoldNotChecked()
                isDroppedNotChecked()
                isPlannedNotChecked()
            }
        }
    }

    @Test
    fun filterDialogConfirmDisabledIfNoFilter() {
        launchActivity()

        seriesList {
            filterSeries {
                open()
            } validate {
                confirmIsEnabled()
            }
            filterSeries {
                clickCurrent()
            } validate {
                isCurrentNotChecked()
                confirmIsDisabled()
            }
        }
    }
}
