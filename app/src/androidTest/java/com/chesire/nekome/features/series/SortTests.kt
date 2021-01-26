package com.chesire.nekome.features.series

import com.chesire.nekome.UITest
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.database.entity.SeriesEntity
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.robots.series.seriesList
import com.chesire.nekome.testing.createSeriesEntity
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class SortTests : UITest() {

    private val seriesData = InitialSortSeriesData()

    override fun setUp() {
        super.setUp()
        runBlocking { series.insert(seriesData.all) }
    }

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

    @Test
    fun sortOptionDefaultSortsInDefaultOrder() {
        // default order is by id
        launchActivity()

        seriesList {
            sortSeries {
                open()
                selectByDefault()
            }
        } validate {
            isInOrder(
                listOf(
                    seriesData.series0.title,
                    seriesData.series1.title,
                    seriesData.series2.title,
                    seriesData.series3.title,
                )
            )
        }
    }

    @Test
    fun sortOptionTitleSortsInTitleOrder() {
        launchActivity()

        seriesList {
            sortSeries {
                open()
                selectByTitle()
            }
        } validate {
            isInOrder(
                listOf(
                    seriesData.series3.title,
                    seriesData.series2.title,
                    seriesData.series1.title,
                    seriesData.series0.title
                )
            )
        }
    }

    @Test
    fun sortOptionStartDateSortsInStartDateOrder() {
        launchActivity()

        seriesList {
            sortSeries {
                open()
                selectByStartDate()
            }
        } validate {
            isInOrder(
                listOf(
                    seriesData.series1.title,
                    seriesData.series2.title,
                    seriesData.series3.title,
                    seriesData.series0.title,
                )
            )
        }
    }

    @Test
    fun sortOptionEndDateSortsInEndDateOrder() {
        launchActivity()

        seriesList {
            sortSeries {
                open()
                selectByEndDate()
            }
        } validate {
            isInOrder(
                listOf(
                    seriesData.series2.title,
                    seriesData.series1.title,
                    seriesData.series0.title,
                    seriesData.series3.title
                )
            )
        }
    }
}

// Make an initial set of series items to compare sort with.
// The details don't make much sense, but we only do a compareBy, so this just validates
// that the compareBy works as we expect.
private data class InitialSortSeriesData(
    val series0: SeriesEntity = createSeriesEntity(
        id = 0,
        userId = 0,
        title = "Z The Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Current,
        startDate = "320/01/01",
        endDate = "220/02/02"
    ),
    val series1: SeriesEntity = createSeriesEntity(
        id = 1,
        userId = 1,
        title = "Y The Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Current,
        startDate = "021/01/01",
        endDate = "121/02/02"
    ),
    val series2: SeriesEntity = createSeriesEntity(
        id = 2,
        userId = 2,
        title = "X The Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Current,
        startDate = "122/01/01",
        endDate = "022/02/02"
    ),
    val series3: SeriesEntity = createSeriesEntity(
        id = 3,
        userId = 3,
        title = "W The Anime",
        seriesType = SeriesType.Anime,
        userSeriesStatus = UserSeriesStatus.Current,
        startDate = "223/01/01",
        endDate = "323/02/02"
    )
) {
    /**
     * Returns all of the series objects in a single list.
     */
    val all = listOf(series0, series1, series2, series3)
}
