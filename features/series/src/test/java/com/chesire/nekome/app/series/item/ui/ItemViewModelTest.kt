@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.item.ui

import androidx.lifecycle.SavedStateHandle
import com.chesire.nekome.app.series.item.core.BuildTitleUseCase
import com.chesire.nekome.app.series.item.core.DeleteItemUseCase
import com.chesire.nekome.app.series.item.core.GetImageUseCase
import com.chesire.nekome.app.series.item.core.RetrieveItemUseCase
import com.chesire.nekome.app.series.item.core.UpdateItemUseCase
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.testing.createSeriesDomain
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ItemViewModelTest {

    private val savedStateHandle = mockk<SavedStateHandle>()
    private val retrieveItem = mockk<RetrieveItemUseCase>()
    private val updateItem = mockk<UpdateItemUseCase>()
    private val deleteItem = mockk<DeleteItemUseCase>()
    private val buildTitle = mockk<BuildTitleUseCase>()
    private val getImage = mockk<GetImageUseCase>()
    private lateinit var viewModel: ItemViewModel

    private val seriesDomain = createSeriesDomain()

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { savedStateHandle.get<Int>("seriesId") } returns 123
        coEvery { buildTitle.invoke(any()) } returns seriesDomain.title
        coEvery { retrieveItem(123) } returns seriesDomain
        coEvery { getImage(any()) } returns "imageUrl"
        viewModel = ItemViewModel(
            savedStateHandle,
            retrieveItem,
            updateItem,
            deleteItem,
            buildTitle,
            getImage
        )
    }

    @Test
    fun `When initializing, Then UIState is emitted as expected`() = runTest {
        val state = viewModel.uiState.value

        assertEquals(seriesDomain.userId, state.id)
        assertEquals(seriesDomain.title, state.title)
        assertEquals("Anime  -  TV  -  Current", state.subtitle)
        assertEquals("imageUrl", state.imageUrl)
        assertEquals(
            listOf(
                UserSeriesStatus.Current,
                UserSeriesStatus.Completed,
                UserSeriesStatus.OnHold,
                UserSeriesStatus.Dropped,
                UserSeriesStatus.Planned
            ),
            state.possibleSeriesStatus
        )
        assertEquals(seriesDomain.userSeriesStatus, state.seriesStatus)
        assertEquals("0", state.progress)
        assertEquals("-", state.length)
        assertEquals(0f, state.rating)
    }

    @Test
    fun `When ConfirmPressed, On success, Then finish screen is emitted`() = runTest {
        coEvery { updateItem(any()) } returns Ok(Unit)

        viewModel.execute(ViewAction.ConfirmPressed)

        assertTrue(viewModel.uiState.value.finishScreen)
    }

    @Test
    fun `When ConfirmPressed, On failure, Then error snackbar is emitted`() = runTest {
        coEvery { updateItem(any()) } returns Err(Unit)

        viewModel.execute(ViewAction.ConfirmPressed)

        assertNotNull(viewModel.uiState.value.errorSnackbar)
    }

    @Test
    fun `When DeletePressed, Then delete dialog is shown`() = runTest {
        viewModel.execute(ViewAction.DeletePressed)

        assertTrue(viewModel.uiState.value.deleteDialog.show)
    }

    @Test
    fun `When HandleDeleteResult, On result is false, Then delete dialog is hidden`() = runTest {
        viewModel.execute(ViewAction.OnDeleteResult(false))

        assertFalse(viewModel.uiState.value.deleteDialog.show)
    }

    @Test
    fun `When HandleDeleteResult, On delete is successful, Then finish screen is emitted`() =
        runTest {
            coEvery { deleteItem(any()) } returns Ok(Unit)

            viewModel.execute(ViewAction.OnDeleteResult(true))

            assertTrue(viewModel.uiState.value.finishScreen)
        }

    @Test
    fun `When HandleDeleteResult, On delete fails, Then error snackbar is emitted`() = runTest {
        coEvery { deleteItem(any()) } returns Err(Unit)

        viewModel.execute(ViewAction.OnDeleteResult(true))

        assertNotNull(viewModel.uiState.value.errorSnackbar)
    }

    @Test
    fun `When handleProgressChanged, On progress less than max, Then new progress is emitted`() =
        runTest {
            viewModel.execute(ViewAction.ProgressChanged("1"))

            assertEquals("1", viewModel.uiState.value.progress)
        }

    @Test
    fun `When handleProgressChanged, On progress higher than max numbers, Then no new progress is emitted`() =
        runTest {
            viewModel.execute(ViewAction.ProgressChanged("1"))
            viewModel.execute(ViewAction.ProgressChanged("12345"))

            assertEquals("1", viewModel.uiState.value.progress)
        }

    @Test
    fun `When handleRatingChanged, Then new rating is emitted`() = runTest {
        viewModel.execute(ViewAction.RatingChanged(1f))

        assertEquals(1f, viewModel.uiState.value.rating)
    }

    @Test
    fun `When handleSeriesStatusChanged, Then new series status is emitted`() = runTest {
        viewModel.execute(ViewAction.SeriesStatusChanged(UserSeriesStatus.OnHold))

        assertEquals(UserSeriesStatus.OnHold, viewModel.uiState.value.seriesStatus)
    }
}
