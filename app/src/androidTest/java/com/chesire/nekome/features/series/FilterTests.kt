package com.chesire.nekome.features.series

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.database.entity.SeriesEntity
import com.chesire.nekome.robots.series.seriesList
import com.chesire.nekome.testing.createSeriesEntity
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FilterTests : UITest() {

    private val seriesData = InitialFilterSeriesData()

    @get:Rule
    val composeTestRule = createComposeRule()

    override fun setUp() {
        super.setUp()

        runBlocking {
            series.insert(seriesData.all)
        }
    }

    @Test
    fun filterDialogDisplaysWithAllOptions() {
        launchActivity()

        seriesList(composeTestRule) {
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

        seriesList(composeTestRule) {
            filterSeries {
                open()
            } validate {
                isInDefaultState()
            }
        }
    }

    @Test
    fun filterDialogClosesOnConfirm() {
        launchActivity()

        seriesList(composeTestRule) {
            filterSeries {
                open()
                confirm()
            } validate {
                isNotVisible()
            }
        }
    }

    @Test
    fun filterDialogRetainsLastChoices() {
        launchActivity()

        seriesList(composeTestRule) {
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

        seriesList(composeTestRule) {
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

        seriesList(composeTestRule) {
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

    @Test
    fun filterCurrentShowsOnlyCurrentSeries() {
        launchActivity()

        seriesList(composeTestRule) {
            filterSeries {
                open()
                // Default has current already selected
                confirm()
            }
        } validate {
            listCount(1)
            checkSeriesAtIndex(0, seriesData.seriesCurrent.title)
        }
    }

    @Test
    fun filterCompletedShowsOnlyCompletedSeries() {
        launchActivity()

        seriesList(composeTestRule) {
            filterSeries {
                open()
                clickCurrent()
                clickCompleted()
                confirm()
            }
        } validate {
            listCount(1)
            checkSeriesAtIndex(0, seriesData.seriesCompleted.title)
        }
    }

    @Test
    fun filterOnHoldShowsOnlyOnHoldSeries() {
        launchActivity()

        seriesList(composeTestRule) {
            filterSeries {
                open()
                clickCurrent()
                clickOnHold()
                confirm()
            }
        } validate {
            listCount(1)
            checkSeriesAtIndex(0, seriesData.seriesOnHold.title)
        }
    }

    @Test
    fun filterDroppedShowsOnlyDroppedSeries() {
        launchActivity()

        seriesList(composeTestRule) {
            filterSeries {
                open()
                clickCurrent()
                clickDropped()
                confirm()
            }
        } validate {
            listCount(1)
            checkSeriesAtIndex(0, seriesData.seriesDropped.title)
        }
    }

    @Test
    fun filterPlannedShowsOnlyPlannedSeries() {
        launchActivity()

        seriesList(composeTestRule) {
            filterSeries {
                open()
                clickCurrent()
                clickPlanned()
                confirm()
            }
        } validate {
            listCount(1)
            checkSeriesAtIndex(0, seriesData.seriesPlanned.title)
        }
    }

    @Test
    fun filterAllShowsAllSeries() {
        launchActivity()

        seriesList(composeTestRule) {
            filterSeries {
                open()
                clickCompleted()
                clickOnHold()
                clickDropped()
                clickPlanned()
                confirm()
            }
        } validate {
            listCount(5)
        }
    }
}

// Make an initial set of series items to check the filter with.
private data class InitialFilterSeriesData(
    val seriesCurrent: SeriesEntity = createSeriesEntity(
        id = 0,
        userId = 0,
        title = "Current Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Current
    ),
    val seriesCompleted: SeriesEntity = createSeriesEntity(
        id = 1,
        userId = 1,
        title = "Completed Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Completed
    ),
    val seriesOnHold: SeriesEntity = createSeriesEntity(
        id = 2,
        userId = 2,
        title = "On hold Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.OnHold
    ),
    val seriesDropped: SeriesEntity = createSeriesEntity(
        id = 3,
        userId = 3,
        title = "Dropped Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Dropped
    ),
    val seriesPlanned: SeriesEntity = createSeriesEntity(
        id = 4,
        userId = 4,
        title = "Planned Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Planned
    )
) {
    /**
     * Returns all of the series objects in a single list.
     */
    val all = listOf(seriesCurrent, seriesCompleted, seriesOnHold, seriesDropped, seriesPlanned)
}
