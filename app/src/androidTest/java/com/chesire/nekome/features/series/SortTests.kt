package com.chesire.nekome.features.series

import com.chesire.nekome.UITest
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.robots.series.seriesList
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class SortTests : UITest() {

    @Test
    fun sortDialogDisplaysWithAllOptions() {
        launchActivity()

        seriesList {
            sortSeries {
                open()
            } validate {
                isVisible()
                isLoadedCorrectly()
            }
        }
    }

    @Test
    fun sortDialogClosesOnOptionSelected() {
        launchActivity()

        seriesList {
            sortSeries {
                open()
                selectByTitle()
            } validate {
                isNotVisible()
            }
        }
    }
}
