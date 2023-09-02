@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.search.host.ui

import com.chesire.nekome.app.search.host.core.HostInitializeUseCase
import com.chesire.nekome.app.search.host.core.InitializeArgs
import com.chesire.nekome.app.search.host.core.RememberSearchGroupUseCase
import com.chesire.nekome.app.search.host.core.RetrieveUserSeriesIdsUseCase
import com.chesire.nekome.app.search.host.core.SearchFailureReason
import com.chesire.nekome.app.search.host.core.SearchSeriesUseCase
import com.chesire.nekome.app.search.host.core.TrackSeriesUseCase
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.search.SearchDomain
import com.chesire.nekome.resources.StringResource
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class HostViewModelTest {

    private val hostInitialize = mockk<HostInitializeUseCase>()
    private val retrieveUserSeriesIds = mockk<RetrieveUserSeriesIdsUseCase>()
    private val rememberSearchGroup = mockk<RememberSearchGroupUseCase>()
    private val searchSeries = mockk<SearchSeriesUseCase>()
    private val trackSeries = mockk<TrackSeriesUseCase>()
    private val mapper = mockk<DomainMapper>()
    private lateinit var viewModel: HostViewModel

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { hostInitialize() } returns InitializeArgs(initialGroup = SearchGroup.Anime)
        every { rememberSearchGroup(any()) } just runs
        coEvery { retrieveUserSeriesIds() } returns flowOf(listOf(5))

        viewModel = HostViewModel(
            hostInitialize,
            retrieveUserSeriesIds,
            rememberSearchGroup,
            searchSeries,
            trackSeries,
            mapper
        )
    }

    @Test
    fun `When SearchGroupChanged, Then new group is saved to use case`() = runTest {
        assertEquals(SearchGroup.Anime, viewModel.uiState.value.searchGroup)

        viewModel.execute(ViewAction.SearchGroupChanged(SearchGroup.Manga))

        verify { rememberSearchGroup(SearchGroup.Manga) }
    }

    @Test
    fun `When SearchGroupChanged, Then new state is emitted`() = runTest {
        assertEquals(SearchGroup.Anime, viewModel.uiState.value.searchGroup)

        viewModel.execute(ViewAction.SearchGroupChanged(SearchGroup.Manga))

        assertEquals(SearchGroup.Manga, viewModel.uiState.value.searchGroup)
    }

    @Test
    fun `When SearchTextUpdated, Then new state is emitted`() = runTest {
        assertEquals("", viewModel.uiState.value.searchText)

        viewModel.execute(ViewAction.SearchTextUpdated("Search text"))

        assertEquals("Search text", viewModel.uiState.value.searchText)
    }

    @Test
    fun `Given api call succeeds, When ExecuteSearch, Then result models are emitted`() =
        runTest {
            val domains = listOf(
                SearchDomain(
                    id = 1,
                    type = SeriesType.Anime,
                    synopsis = "Synopsis",
                    canonicalTitle = "canonicalTitle",
                    otherTitles = emptyMap(),
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
            )
            val expected = listOf(
                ResultModel(
                    id = 1,
                    type = SeriesType.Anime,
                    synopsis = "Synopsis",
                    title = "canonicalTitle",
                    subtype = "Movie",
                    posterImage = "posterImage",
                    canTrack = true,
                    isTracking = false
                )
            )
            coEvery { searchSeries(any(), SearchGroup.Anime) } returns Ok(domains)
            every { mapper.toResultModels(domains, any()) } returns expected

            viewModel.execute(ViewAction.SearchTextUpdated("Search text"))
            viewModel.execute(ViewAction.ExecuteSearch)

            assertEquals(
                expected,
                viewModel.uiState.value.resultModels
            )
        }

    @Test
    fun `Given api call fails, When ExecuteSearch, Then error snackbar state is emitted`() =
        runTest {
            val expected = SnackbarData(StringResource.error_generic)
            coEvery {
                searchSeries(any(), SearchGroup.Anime)
            } returns Err(SearchFailureReason.NetworkError)

            viewModel.execute(ViewAction.SearchTextUpdated("Search text"))
            viewModel.execute(ViewAction.ExecuteSearch)

            assertEquals(
                expected,
                viewModel.uiState.value.errorSnackbar
            )
        }

    @Test
    fun `When ErrorSnackbarObserved, Then error snackbar state is updated`() = runTest {
        coEvery {
            searchSeries(any(), SearchGroup.Anime)
        } returns Err(SearchFailureReason.NetworkError)
        viewModel.execute(ViewAction.SearchTextUpdated("Search text"))
        viewModel.execute(ViewAction.ExecuteSearch)
        assertNotNull(
            viewModel.uiState.value.errorSnackbar
        )

        viewModel.execute(ViewAction.ErrorSnackbarObserved)

        assertNull(viewModel.uiState.value.errorSnackbar)
    }
}
