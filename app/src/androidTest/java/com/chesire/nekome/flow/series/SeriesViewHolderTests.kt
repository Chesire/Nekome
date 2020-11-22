package com.chesire.nekome.flow.series

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.login
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.testing.createSeriesModel
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModule::class, KitsuModule::class)
@RunWith(AndroidJUnit4::class)
class SeriesViewHolderTests {
    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var authProvider: AuthProvider

    @Inject
    lateinit var seriesDao: SeriesDao

    val fakeLibrary = mockk<LibraryApi>()

    @Before
    fun setUp() {
        hilt.inject()
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
            seriesDao.insert(createSeriesModel(startDate = "2020-01-31", endDate = ""))
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
            seriesDao.insert(createSeriesModel(startDate = "", endDate = ""))
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
            seriesDao.insert(createSeriesModel(startDate = expectedDate, endDate = expectedDate))
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
            seriesDao.insert(createSeriesModel(progress = 3, totalLength = 0))
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
            seriesDao.insert(createSeriesModel(progress = 3, totalLength = 3))
        }
        launchActivity()

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
            fakeLibrary.update(any(), any(), any())
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
            seriesDao.insert(createSeriesModel(progress = 1, totalLength = 3))
        }
        coEvery {
            fakeLibrary.update(any(), any(), any())
        } coAnswers {
            Resource.Success(createSeriesModel(progress = 2, totalLength = 3))
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

    @Module
    @InstallIn(ApplicationComponent::class)
    inner class FakeKitsuModule {
        @Provides
        fun providesAuth() = mockk<AuthApi>()

        @Provides
        fun providesLibrary() = fakeLibrary

        @Provides
        fun providesSearch() = mockk<SearchApi>()

        @Provides
        fun providesTrending() = mockk<TrendingApi>()

        @Provides
        fun providesUser() = mockk<UserApi>()
    }
}
