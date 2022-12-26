@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.login.syncing.ui

import com.chesire.nekome.app.login.syncing.core.RetrieveAvatarUseCase
import com.chesire.nekome.app.login.syncing.core.SyncSeriesUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SyncingViewModelTest {

    private val retrieveAvatar = mockk<RetrieveAvatarUseCase>()
    private val syncSeries = mockk<SyncSeriesUseCase>()
    private lateinit var viewModel: SyncingViewModel

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `When init, Then avatar is emitted`() = runTest {
        val expected = "avatarUrl"
        coEvery { retrieveAvatar() } returns expected
        coEvery { syncSeries() } returns listOf()

        viewModel = SyncingViewModel(retrieveAvatar, syncSeries)
        val result = viewModel.uiState.value.avatar

        assertEquals(expected, result)
    }

    @Test
    fun `When init finished, Then finished syncing is emitted`() = runTest {
        coEvery { retrieveAvatar() } returns ""
        coEvery { syncSeries() } returns listOf()

        viewModel = SyncingViewModel(retrieveAvatar, syncSeries)
        val result = viewModel.uiState.value.finishedSyncing

        assertEquals(true, result)
    }

    @Test
    fun `When observeFinishedSyncing, Then new state is emitted`() = runTest {
        coEvery { retrieveAvatar() } returns ""
        coEvery { syncSeries() } returns listOf()

        viewModel = SyncingViewModel(retrieveAvatar, syncSeries)
        assertEquals(true, viewModel.uiState.value.finishedSyncing)
        viewModel.observeFinishedSyncing()

        assertEquals(null, viewModel.uiState.value.finishedSyncing)
    }
}
