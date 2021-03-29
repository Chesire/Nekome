package com.chesire.nekome.features.series

import com.chesire.nekome.R
import com.chesire.nekome.UITest
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.series.remote.SeriesApi
import com.chesire.nekome.helpers.creation.createSeriesDomain
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.testing.createSeriesEntity
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test

@HiltAndroidTest
@UninstallModules(LibraryModule::class)
class SeriesViewHolderTests : UITest() {

    @BindValue
    val seriesApi = mockk<SeriesApi>()

    @Test
    fun itemDisplaysCorrectly() {
        val expectedTitle = "expectedTitle"
        val expectedSubtype = Subtype.OVA
        val expectedDate = "2020-01-31 - 2020-02-31"
        val expectedProgress = "3 / 10"

        runBlocking {
            series.insert(
                createSeriesEntity(
                    title = expectedTitle,
                    subType = expectedSubtype,
                    progress = 3,
                    totalLength = 10,
                    startDate = "2020-01-31",
                    endDate = "2020-02-31"
                )
            )
        }
        launchActivity()

        assertListNotEmpty(R.id.listContent)
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesTitle,
            expectedTitle
        )
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesSubtype,
            expectedSubtype.name
        )
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesProgress,
            expectedProgress
        )
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesDate,
            expectedDate
        )
        assertDisplayed(R.id.seriesPlusOne)
    }

    @Test
    fun itemWithNoEndDateShowsOngoing() {
        val expectedDate = "2020-01-31 - ONGOING"

        runBlocking {
            series.insert(createSeriesEntity(startDate = "2020-01-31", endDate = ""))
        }
        launchActivity()

        assertListNotEmpty(R.id.listContent)
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesDate,
            expectedDate
        )
    }

    @Test
    fun itemWithNoDatesShowsUnknown() {
        val expectedDate = "UNKNOWN"

        runBlocking {
            series.insert(createSeriesEntity(startDate = "", endDate = ""))
        }
        launchActivity()

        assertListNotEmpty(R.id.listContent)
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesDate,
            expectedDate
        )
    }

    @Test
    fun itemWithMatchingDatesShowsJustOneDate() {
        val expectedDate = "2020-01-31"

        runBlocking {
            series.insert(createSeriesEntity(startDate = expectedDate, endDate = expectedDate))
        }
        launchActivity()

        assertListNotEmpty(R.id.listContent)
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesDate,
            expectedDate
        )
    }

    @Test
    fun itemWithNoLengthShowsUnknown() {
        val expectedProgress = "3 / -"

        runBlocking {
            series.insert(createSeriesEntity(progress = 3, totalLength = 0))
        }
        launchActivity()

        assertListNotEmpty(R.id.listContent)
        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesProgress,
            expectedProgress
        )
    }

    @Test
    fun itemProgressMaxedHidesIncrementButton() {
        runBlocking {
            series.insert(createSeriesEntity(progress = 3, totalLength = 3))
        }
        launchActivity()

        assertListNotEmpty(R.id.listContent)
        assertNotDisplayed(R.id.seriesPlusOne)
    }

    @Test
    @Ignore("Leave for now as unsure how to ensure that the progress bar appears")
    fun itemChangesToLoadingViewWhileUpdating() {
        runBlocking {
            series.insert(createSeriesEntity(progress = 1, totalLength = 3))
        }
        coEvery {
            seriesApi.update(any(), any(), any(), any())
        } coAnswers {
            Resource.Error("")
        }
        launchActivity()

        assertNotDisplayed(R.id.seriesProgressBar)
        clickListItemChild(R.id.listContent, 0, R.id.seriesPlusOne)

        assertDisplayed(R.id.seriesProgressBar)
    }

    @Test
    @Ignore("Leave for now as unsure how to ensure that the progress bar appears")
    fun itemRevertsFromLoadingViewOnceUpdated() {
        // See ignore
    }

    @Test
    fun seriesProgressIncrementsByOneOnUpdate() {
        val initialProgress = "1 / 3"
        val expectedProgress = "2 / 3"
        runBlocking {
            series.insert(
                createSeriesEntity(
                    999,
                    999,
                    SeriesType.Anime,
                    Subtype.TV,
                    "slug",
                    "title",
                    SeriesStatus.Current,
                    UserSeriesStatus.Current,
                    1,
                    3,
                    0,
                    ImageModel.empty,
                    "",
                    ""
                )
            )
        }
        coEvery {
            seriesApi.update(any(), any(), any(), any())
        } coAnswers {
            Resource.Success(
                createSeriesDomain(
                    999,
                    999,
                    SeriesType.Anime,
                    Subtype.TV,
                    "slug",
                    "title",
                    SeriesStatus.Current,
                    UserSeriesStatus.Current,
                    2,
                    3,
                    5,
                    ImageModel.empty,
                    "",
                    ""
                )
            )
        }
        launchActivity()

        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesProgress,
            initialProgress
        )
        clickListItemChild(R.id.listContent, 0, R.id.seriesPlusOne)

        assertDisplayedAtPosition(
            R.id.listContent,
            0,
            R.id.seriesProgress,
            expectedProgress
        )
    }
}
