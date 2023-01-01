@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.search.host.ui

import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.host.core.HostInitializeUseCase
import com.chesire.nekome.app.search.host.core.InitializeArgs
import com.chesire.nekome.app.search.host.core.RememberSearchGroupUseCase
import com.chesire.nekome.app.search.host.core.SearchFailureReason
import com.chesire.nekome.app.search.host.core.SearchSeriesUseCase
import com.chesire.nekome.app.search.host.core.model.SearchGroup
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
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class HostViewModelTest {

    private val hostInitialize = mockk<HostInitializeUseCase>()
    private val rememberSearchGroup = mockk<RememberSearchGroupUseCase>()
    private val searchSeriesUseCase = mockk<SearchSeriesUseCase>()
    private lateinit var viewModel: HostViewModel

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { hostInitialize() } returns InitializeArgs(initialGroup = SearchGroup.Anime)
        every { rememberSearchGroup(any()) } just runs

        viewModel = HostViewModel(hostInitialize, rememberSearchGroup, searchSeriesUseCase)
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
    fun `Given api call succeeds, When ExecuteSearch, Then navigation state is emitted`() =
        runTest {
            coEvery { searchSeriesUseCase(any(), SearchGroup.Anime) } returns Ok(emptyList())

            viewModel.execute(ViewAction.SearchTextUpdated("Search text"))
            viewModel.execute(ViewAction.ExecuteSearch)

            assertEquals(
                NavigationData("Search text", emptyList()),
                viewModel.uiState.value.navigateScreenEvent
            )
        }

    @Test
    fun `Given api call fails, When ExecuteSearch, Then error snackbar state is emitted`() =
        runTest {
            coEvery {
                searchSeriesUseCase(any(), SearchGroup.Anime)
            } returns Err(SearchFailureReason.NetworkError)

            viewModel.execute(ViewAction.SearchTextUpdated("Search text"))
            viewModel.execute(ViewAction.ExecuteSearch)

            assertEquals(
                R.string.error_generic,
                viewModel.uiState.value.errorSnackbarMessage
            )
        }

    @Test
    fun `When ErrorSnackbarObserved, Then error snackbar state is updated`() = runTest {
        coEvery {
            searchSeriesUseCase(any(), SearchGroup.Anime)
        } returns Err(SearchFailureReason.NetworkError)
        viewModel.execute(ViewAction.SearchTextUpdated("Search text"))
        viewModel.execute(ViewAction.ExecuteSearch)
        assertEquals(
            R.string.error_generic,
            viewModel.uiState.value.errorSnackbarMessage
        )

        viewModel.execute(ViewAction.ErrorSnackbarObserved)

        assertNull(viewModel.uiState.value.errorSnackbarMessage)
    }

    @Test
    fun `When NavigationObserved, Then navigation state is updated`() = runTest {
        coEvery { searchSeriesUseCase(any(), SearchGroup.Anime) } returns Ok(emptyList())
        viewModel.execute(ViewAction.SearchTextUpdated("Search text"))
        viewModel.execute(ViewAction.ExecuteSearch)
        assertEquals(
            NavigationData("Search text", emptyList()),
            viewModel.uiState.value.navigateScreenEvent
        )

        viewModel.execute(ViewAction.NavigationObserved)

        assertNull(viewModel.uiState.value.navigateScreenEvent)
    }
}
