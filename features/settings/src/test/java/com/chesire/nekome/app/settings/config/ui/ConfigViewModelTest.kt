@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.settings.config.ui

import com.chesire.nekome.app.settings.config.core.PreferenceModel
import com.chesire.nekome.app.settings.config.core.RetrievePreferencesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultHomeScreenUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultSeriesStateUseCase
import com.chesire.nekome.app.settings.config.core.UpdateRateSeriesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateThemeUseCase
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.Theme
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ConfigViewModelTest {

    private val retrievePreferences = mockk<RetrievePreferencesUseCase>()
    private val updateRateSeries = mockk<UpdateRateSeriesUseCase>(relaxed = true)
    private val updateTheme = mockk<UpdateThemeUseCase>(relaxed = true)
    private val updateDefaultHomeScreen = mockk<UpdateDefaultHomeScreenUseCase>(relaxed = true)
    private val updateDefaultSeriesState = mockk<UpdateDefaultSeriesStateUseCase>(relaxed = true)
    private lateinit var viewModel: ConfigViewModel

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every {
            retrievePreferences()
        } returns flowOf(
            PreferenceModel(
                theme = Theme.Dark,
                defaultHomeScreen = HomeScreenOptions.Anime,
                defaultSeriesStatus = UserSeriesStatus.Current,
                shouldRateSeries = false
            )
        )

        viewModel = ConfigViewModel(
            retrievePreferences,
            updateRateSeries,
            updateTheme,
            updateDefaultHomeScreen,
            updateDefaultSeriesState
        )
    }

    @Test
    fun `When initialize, uiState is expected defaults`() = runTest {
        val expected = UIState(
            themeValue = Theme.Dark,
            showThemeDialog = false,
            defaultHomeValue = HomeScreenOptions.Anime,
            showDefaultHomeDialog = false,
            defaultSeriesStatusValue = UserSeriesStatus.Current,
            showDefaultSeriesStatusDialog = false,
            rateSeriesValue = false
        )

        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `When execute#OnThemeClicked, Then dialog is shown`() = runTest {
        viewModel.execute(ViewAction.OnThemeClicked)

        assertTrue(viewModel.uiState.value.showThemeDialog)
    }

    @Test
    fun `When execute#OnThemeChanged, Then dialog is hidden`() = runTest {
        viewModel.execute(ViewAction.OnThemeChanged(Theme.Light))

        assertFalse(viewModel.uiState.value.showThemeDialog)
    }

    @Test
    fun `When execute#OnThemeChanged, Then value is updated in use case`() = runTest {
        viewModel.execute(ViewAction.OnThemeChanged(Theme.Light))

        coVerify { updateTheme(Theme.Light) }
    }

    @Test
    fun `When execute#OnDefaultHomeScreenClicked, Then dialog is shown`() = runTest {
        viewModel.execute(ViewAction.OnDefaultHomeScreenClicked)

        assertTrue(viewModel.uiState.value.showDefaultHomeDialog)
    }

    @Test
    fun `When execute#OnDefaultHomeScreenChanged, Then dialog is hidden`() = runTest {
        viewModel.execute(ViewAction.OnDefaultHomeScreenChanged(HomeScreenOptions.Manga))

        assertFalse(viewModel.uiState.value.showDefaultHomeDialog)
    }

    @Test
    fun `When execute#OnDefaultHomeScreenChanged, Then value is updated in use case`() = runTest {
        viewModel.execute(ViewAction.OnDefaultHomeScreenChanged(HomeScreenOptions.Manga))

        coVerify { updateDefaultHomeScreen(HomeScreenOptions.Manga) }
    }

    @Test
    fun `When execute#OnDefaultSeriesStatusClicked, Then dialog is shown`() = runTest {
        viewModel.execute(ViewAction.OnDefaultSeriesStatusClicked)

        assertTrue(viewModel.uiState.value.showDefaultSeriesStatusDialog)
    }

    @Test
    fun `When execute#OnDefaultSeriesStatusChanged, Then dialog is hidden`() = runTest {
        viewModel.execute(ViewAction.OnDefaultSeriesStatusChanged(UserSeriesStatus.Dropped))

        assertFalse(viewModel.uiState.value.showDefaultSeriesStatusDialog)
    }

    @Test
    fun `When execute#OnDefaultSeriesStatusChanged, Then value is updated in use case`() = runTest {
        viewModel.execute(ViewAction.OnDefaultSeriesStatusChanged(UserSeriesStatus.Dropped))

        coVerify { updateDefaultSeriesState(UserSeriesStatus.Dropped) }
    }
}
