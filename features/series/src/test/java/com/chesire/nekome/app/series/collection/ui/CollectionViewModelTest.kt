@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.ui

import androidx.lifecycle.SavedStateHandle
import com.chesire.nekome.app.series.collection.core.CollectSeriesUseCase
import com.chesire.nekome.app.series.collection.core.CurrentFiltersUseCase
import com.chesire.nekome.app.series.collection.core.CurrentSortUseCase
import com.chesire.nekome.app.series.collection.core.FilterSeriesUseCase
import com.chesire.nekome.app.series.collection.core.IncrementSeriesUseCase
import com.chesire.nekome.app.series.collection.core.RefreshSeriesUseCase
import com.chesire.nekome.app.series.collection.core.ShouldRateSeriesUseCase
import com.chesire.nekome.app.series.collection.core.SortSeriesUseCase
import com.chesire.nekome.app.series.collection.core.UpdateFiltersUseCase
import com.chesire.nekome.app.series.collection.core.UpdateSortUseCase
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.SortOption
import com.chesire.nekome.resources.StringResource
import com.chesire.nekome.testing.createSeriesDomain
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CollectionViewModelTest {

    private val savedStateHandle = mockk<SavedStateHandle>()
    private val collectSeries = mockk<CollectSeriesUseCase>()
    private val incrementSeries = mockk<IncrementSeriesUseCase>()
    private val refreshSeries = mockk<RefreshSeriesUseCase>()
    private val shouldRateSeries = mockk<ShouldRateSeriesUseCase>()
    private val currentSort = mockk<CurrentSortUseCase>()
    private val sortSeries = mockk<SortSeriesUseCase>()
    private val updateSort = mockk<UpdateSortUseCase>()
    private val currentFilters = mockk<CurrentFiltersUseCase>()
    private val filterSeries = mockk<FilterSeriesUseCase>()
    private val updateFilters = mockk<UpdateFiltersUseCase>()
    private val domainMapper = mockk<DomainMapper>()
    private lateinit var viewModel: CollectionViewModel

    private val models = listOf(
        Series(
            userId = 0,
            title = "Title",
            posterImageUrl = "",
            subtype = Subtype.Movie.name,
            progress = "0 / 12",
            startDate = "",
            endDate = "",
            rating = 0,
            showPlusOne = true,
            isUpdating = false
        )
    )

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())

        val initialModels = listOf(
            createSeriesDomain(id = 0),
            createSeriesDomain(id = 1)
        )
        every { savedStateHandle.get<SeriesType>("seriesType") } returns SeriesType.Anime
        every { collectSeries() } returns flowOf(initialModels)
        every { filterSeries(initialModels, SeriesType.Anime) } returns flowOf(initialModels)
        every { sortSeries(initialModels) } returns flowOf(initialModels)
        every { domainMapper.toSeries(initialModels) } returns models

        viewModel = CollectionViewModel(
            savedStateHandle,
            collectSeries,
            incrementSeries,
            refreshSeries,
            shouldRateSeries,
            currentSort,
            sortSeries,
            updateSort,
            currentFilters,
            filterSeries,
            updateFilters,
            domainMapper
        )
    }

    @Test
    fun `on init, uiState is emitted`() = runTest {
        assertEquals(
            StringResource.nav_anime,
            viewModel.uiState.value.screenTitle
        )
        assertEquals(models, viewModel.uiState.value.models)
    }

    @Test
    fun `on execute PerformSeriesRefresh, with success, then isRefreshing is false`() = runTest {
        coEvery { refreshSeries() } returns Ok(Unit)

        viewModel.execute(ViewAction.PerformSeriesRefresh)

        assertFalse(viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun `on execute PerformSeriesRefresh, with failure, then error is emitted`() = runTest {
        coEvery { refreshSeries() } returns Err(Unit)

        viewModel.execute(ViewAction.PerformSeriesRefresh)

        assertFalse(viewModel.uiState.value.isRefreshing)
        assertEquals(
            StringResource.series_list_refresh_error,
            viewModel.uiState.value.errorSnackbar?.stringRes
        )
    }

    @Test
    fun `on execute SeriesPressed, then series detail is emitted`() = runTest {
        val model = models.first()

        viewModel.execute(ViewAction.SeriesPressed(model))

        assertEquals(
            SeriesDetails(
                show = true,
                seriesId = model.userId,
                seriesTitle = model.title
            ),
            viewModel.uiState.value.seriesDetails
        )
    }

    @Test
    fun `on execute SeriesNavigationObserved, then series detail is nulled out`() = runTest {
        val model = models.first()

        viewModel.execute(ViewAction.SeriesPressed(model))
        assertEquals(
            SeriesDetails(
                show = true,
                seriesId = model.userId,
                seriesTitle = model.title
            ),
            viewModel.uiState.value.seriesDetails
        )
        viewModel.execute(ViewAction.SeriesNavigationObserved)

        assertNull(viewModel.uiState.value.seriesDetails)
    }

    @Test
    fun `on execute IncrementSeriesPressed, if should rate series, then rating dialog is shown`() =
        runTest {
            val model = models.first()
            coEvery { shouldRateSeries(model.userId) } returns true

            viewModel.execute(ViewAction.IncrementSeriesPressed(model))

            assertEquals(
                Rating(
                    series = model,
                    show = true
                ),
                viewModel.uiState.value.ratingDialog
            )
        }

    @Test
    fun `on execute IncrementSeriesPressed, if increment series is successful, no error is emitted`() =
        runTest {
            val model = models.first()
            coEvery { shouldRateSeries(model.userId) } returns false
            coEvery { incrementSeries(model.userId, null) } returns Ok(createSeriesDomain())

            viewModel.execute(ViewAction.IncrementSeriesPressed(model))

            coVerify { incrementSeries(model.userId, null) }
            assertNull(viewModel.uiState.value.errorSnackbar)
        }

    @Test
    fun `on execute IncrementSeriesPressed, if increment series fails, then error is emitted`() =
        runTest {
            val model = models.first()
            coEvery { shouldRateSeries(model.userId) } returns false
            coEvery { incrementSeries(model.userId, null) } returns Err(Unit)

            viewModel.execute(ViewAction.IncrementSeriesPressed(model))

            coVerify { incrementSeries(model.userId, null) }
            assertEquals(
                SnackbarData(
                    stringRes = StringResource.series_list_try_again,
                    formatText = model.title
                ),
                viewModel.uiState.value.errorSnackbar
            )
        }

    @Test
    fun `on execute IncrementSeriesWithRating, then increment series is invoked with new rating`() =
        runTest {
            val model = models.first()
            coEvery { shouldRateSeries(model.userId) } returns false
            coEvery { incrementSeries(model.userId, 5) } returns Ok(createSeriesDomain())

            viewModel.execute(ViewAction.IncrementSeriesWithRating(model, 5))

            coVerify { incrementSeries(model.userId, 5) }
            assertNull(viewModel.uiState.value.errorSnackbar)
        }

    @Test
    fun `on execute SortPressed, then show sort dialog is emitted`() = runTest {
        coEvery { currentSort() } returns SortOption.Rating

        viewModel.execute(ViewAction.SortPressed)

        assertEquals(
            Sort(
                show = true,
                currentSort = SortOption.Rating,
                sortOptions = listOf(
                    SortOption.Default,
                    SortOption.Title,
                    SortOption.StartDate,
                    SortOption.EndDate,
                    SortOption.Rating
                )
            ),
            viewModel.uiState.value.sortDialog
        )
    }

    @Test
    fun `on execute PerformSort, if sortOption is null, then sort dialog is closed`() = runTest {
        coEvery { updateSort(any()) } just runs

        viewModel.execute(ViewAction.PerformSort(null))

        coVerify(exactly = 0) { updateSort(any()) }
        assertFalse(viewModel.uiState.value.sortDialog.show)
    }

    @Test
    fun `on execute PerformSort, if sortOption is passed in, then sort is updated and the sort dialog is closed`() =
        runTest {
            coEvery { updateSort(any()) } just runs

            viewModel.execute(ViewAction.PerformSort(SortOption.EndDate))

            coVerify(exactly = 1) { updateSort(SortOption.EndDate) }
            assertFalse(viewModel.uiState.value.sortDialog.show)
        }

    @Test
    fun `on execute FilterPressed, then show filter dialog is emitted`() = runTest {
        coEvery { currentFilters() } returns mapOf()

        viewModel.execute(ViewAction.FilterPressed)

        assertTrue(viewModel.uiState.value.filterDialog.show)
    }

    @Test
    fun `on execute PerformFilter, if filterOptions is null, then filter dialog is closed`() =
        runTest {
            coEvery { updateFilters(any()) } just runs

            viewModel.execute(ViewAction.PerformFilter(null))

            coVerify(exactly = 0) { updateFilters(any()) }
            assertFalse(viewModel.uiState.value.filterDialog.show)
        }

    @Test
    fun `on execute PerformFilter, if filterOptions is passed in, then filter is updated and the filter dialog is closed`() =
        runTest {
            coEvery { updateFilters(any()) } just runs

            viewModel.execute(
                ViewAction.PerformFilter(
                    listOf(
                        FilterOption(
                            UserSeriesStatus.Dropped,
                            true
                        )
                    )
                )
            )

            coVerify { updateFilters(any()) }
            assertFalse(viewModel.uiState.value.filterDialog.show)
        }
}
