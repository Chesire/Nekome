@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.search.results.ui

import androidx.lifecycle.SavedStateHandle
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.app.search.results.core.RetrieveUserSeriesIdsUseCase
import com.chesire.nekome.app.search.results.core.TrackSeriesUseCase
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ResultsViewModelTest {

    private val savedStateHandle = mockk<SavedStateHandle>()
    private val retrieveUserSeriesIds = mockk<RetrieveUserSeriesIdsUseCase>()
    private val trackSeries = mockk<TrackSeriesUseCase>()
    private lateinit var viewModel: ResultsViewModel

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every {
            savedStateHandle.get<Array<SearchModel>>("searchResults")
        } returns arrayOf(searchModel.copy(id = 1), searchModel.copy(id = 2))
        every { retrieveUserSeriesIds() } returns flowOf(listOf(1))

        viewModel = ResultsViewModel(savedStateHandle, retrieveUserSeriesIds, trackSeries)
    }

    @Test
    fun `on initialize, models are emitted as expected`() = runTest {
        val expected = UIState(
            models = listOf(
                resultModel.copy(id = 1, canTrack = false),
                resultModel.copy(id = 2)
            ),
            errorSnackbar = null
        )

        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `when trackNewSeries, on success, result model canTrack is updated`() = runTest {
        val expected = UIState(
            models = listOf(
                resultModel.copy(id = 1, canTrack = false),
                resultModel.copy(id = 2, canTrack = false)
            ),
            errorSnackbar = null
        )
        coEvery { trackSeries(2, SeriesType.Anime) } returns Ok(Unit)

        viewModel.trackNewSeries(resultModel.copy(id = 2))

        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `when trackNewSeries, on failure, failure state is emitted`() = runTest {
        val expected = UIState(
            models = listOf(
                resultModel.copy(id = 1, canTrack = false),
                resultModel.copy(id = 2, canTrack = true)
            ),
            errorSnackbar = SnackbarData(
                stringRes = R.string.results_failure,
                formatText = resultModel.canonicalTitle
            )
        )
        coEvery { trackSeries(2, SeriesType.Anime) } returns Err(Unit)

        viewModel.trackNewSeries(resultModel.copy(id = 2))

        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `when errorSnackbarObserved, new ui state is emitted`() = runTest {
        coEvery { trackSeries(2, SeriesType.Anime) } returns Err(Unit)

        viewModel.trackNewSeries(resultModel.copy(id = 2))
        viewModel.errorSnackbarObserved()

        assertNull(viewModel.uiState.value.errorSnackbar)
    }

    private companion object {
        val searchModel = SearchModel(
            id = 0,
            type = SeriesType.Anime,
            synopsis = "Synopsis",
            canonicalTitle = "Title",
            subtype = Subtype.Movie,
            posterImage = ImageModel(
                tiny = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                ),
                small = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                ),
                medium = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                ),
                large = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                )
            )
        )

        val resultModel = ResultModel(
            id = 0,
            type = SeriesType.Anime,
            synopsis = "Synopsis",
            canonicalTitle = "Title",
            subtype = Subtype.Movie.name,
            posterImage = ImageModel(
                tiny = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                ),
                small = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                ),
                medium = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                ),
                large = ImageModel.ImageData(
                    url = "",
                    width = 0,
                    height = 0
                )
            ),
            canTrack = true,
            isTracking = false
        )
    }
}
