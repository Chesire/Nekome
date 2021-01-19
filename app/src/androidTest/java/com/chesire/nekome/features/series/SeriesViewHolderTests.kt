package com.chesire.nekome.features.series

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chesire.nekome.R
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.helpers.creation.createLibraryDomain
import com.chesire.nekome.helpers.launchActivity
import com.chesire.nekome.helpers.login
import com.chesire.nekome.injection.DatabaseModule
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.kitsu.AuthProvider
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.testing.createSeriesEntity
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
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
@UninstallModules(DatabaseModule::class, LibraryModule::class)
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
            seriesDao.insert(createSeriesEntity(startDate = "2020-01-31", endDate = ""))
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
            seriesDao.insert(createSeriesEntity(startDate = "", endDate = ""))
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
            seriesDao.insert(createSeriesEntity(startDate = expectedDate, endDate = expectedDate))
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
            seriesDao.insert(createSeriesEntity(progress = 3, totalLength = 0))
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
            seriesDao.insert(createSeriesEntity(progress = 3, totalLength = 3))
        }
        launchActivity()

        assertListNotEmpty(R.id.listContent)
        assertNotDisplayed(R.id.seriesPlusOne)
    }

    @Test
    @Ignore("Leave for now as unsure how to ensure that the progress bar appears")
    fun itemChangesToLoadingViewWhileUpdating() {
        runBlocking {
            seriesDao.insert(createSeriesEntity(progress = 1, totalLength = 3))
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
            seriesDao.insert(
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
                    ImageModel.empty,
                    "",
                    ""
                )
            )
        }
        coEvery {
            fakeLibrary.update(any(), any(), any())
        } coAnswers {
            Resource.Success(
                createLibraryDomain(
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

    @Module
    @InstallIn(SingletonComponent::class)
    inner class FakeKitsuModule {

        @Provides
        fun providesLibrary() = fakeLibrary
    }
}
