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
import com.chesire.nekome.testing.createSeriesModel
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import kotlinx.coroutines.runBlocking
import org.junit.Before
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

        assertListNotEmpty(R.id.fragmentSeriesListRecyclerView)
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesTitle,
            expectedTitle
        )
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesSubtype,
            expectedSubtype.name
        )
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesProgress,
            expectedProgress
        )
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesDate,
            expectedDate
        )
        assertDisplayed(R.id.adapterItemSeriesPlusOne)
    }

    @Test
    fun itemWithNoEndDateShowsUnknown() {
        val expectedDate = "2020-01-31 - ????-??-??"

        runBlocking {
            seriesDao.insert(createSeriesModel(startDate = "2020-01-31", endDate = ""))
        }
        activity.launchActivity(null)

        assertListNotEmpty(R.id.fragmentSeriesListRecyclerView)
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesDate,
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

        assertListNotEmpty(R.id.fragmentSeriesListRecyclerView)
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesDate,
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

        assertListNotEmpty(R.id.fragmentSeriesListRecyclerView)
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesDate,
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

        assertListNotEmpty(R.id.fragmentSeriesListRecyclerView)
        assertDisplayedAtPosition(
            R.id.fragmentSeriesListRecyclerView,
            0,
            R.id.adapterItemSeriesProgress,
            expectedProgress
        )
    }

    @Test
    fun itemProgressMaxedHidesIncrementButton() {
        runBlocking {
            seriesDao.insert(createSeriesModel(progress = 3, totalLength = 3))
        }
        activity.launchActivity(null)

        assertListNotEmpty(R.id.fragmentSeriesListRecyclerView)
        assertNotDisplayed(R.id.adapterItemSeriesPlusOne)
    }
}
