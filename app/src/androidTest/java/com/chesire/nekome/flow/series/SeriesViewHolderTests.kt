package com.chesire.nekome.flow.series

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.chesire.nekome.R
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.flow.Activity
import com.chesire.nekome.helpers.injector
import com.chesire.nekome.helpers.login
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.LibraryApi
import com.chesire.nekome.testing.createSeriesModel
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class SeriesViewHolderTests {
    @get:Rule
    val activity = ActivityTestRule(Activity::class.java, false, false)

    @Inject
    lateinit var authProvider: AuthProvider

    @Inject
    lateinit var seriesDao: SeriesDao

    @Inject
    lateinit var library: LibraryApi

    @Before
    fun setUp() {
        injector.inject(this)
        authProvider.login()
    }

    @Test
    fun itemDisplaysCorrectly() {
        val expectedTitle = "expectedTitle"
        val expectedSubtype = Subtype.OVA
        val expectedDate = "2020-01-31 - 2020-02-31"
        val expectedProgress = "3 / 10"

        runBlocking {
            seriesDao.insert(
                createSeriesModel(
                    title = expectedTitle,
                    subType = expectedSubtype,
                    progress = 3,
                    totalLength = 10,
                    startDate = "2020-01-31",
                    endDate = "2020-02-31"
                )
            )
        }
        activity.launchActivity(null)

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
    fun itemWithNoEndDateShowsUnknown() {
        val expectedDate = "2020-01-31 - ????-??-??"

        runBlocking {
            seriesDao.insert(createSeriesModel(startDate = "2020-01-31", endDate = ""))
        }
        activity.launchActivity(null)

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
        val expectedDate = "????-??-?? - ????-??-??"

        runBlocking {
            seriesDao.insert(createSeriesModel(startDate = "", endDate = ""))
        }
        activity.launchActivity(null)

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
            seriesDao.insert(createSeriesModel(startDate = expectedDate, endDate = expectedDate))
        }
        activity.launchActivity(null)

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
            seriesDao.insert(createSeriesModel(progress = 3, totalLength = 0))
        }
        activity.launchActivity(null)

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
            seriesDao.insert(createSeriesModel(progress = 3, totalLength = 3))
        }
        activity.launchActivity(null)

        assertListNotEmpty(R.id.listContent)
        assertNotDisplayed(R.id.seriesPlusOne)
    }

    @Test
    @Ignore("Leave for now as unsure how to ensure that the progress bar appears")
    fun itemChangesToLoadingViewWhileUpdating() {
        runBlocking {
            seriesDao.insert(createSeriesModel(progress = 1, totalLength = 3))
        }
        coEvery {
            library.update(any(), any(), any())
        } coAnswers {
            Resource.Error("")
        }
        activity.launchActivity(null)

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
            seriesDao.insert(createSeriesModel(progress = 1, totalLength = 3))
        }
        coEvery {
            library.update(any(), any(), any())
        } coAnswers {
            Resource.Success(createSeriesModel(progress = 2, totalLength = 3))
        }
        activity.launchActivity(null)

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
