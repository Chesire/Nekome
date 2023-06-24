@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.settings.config.ui

import com.chesire.nekome.app.settings.config.LogoutExecutor
import com.chesire.nekome.app.settings.config.core.PreferenceModel
import com.chesire.nekome.app.settings.config.core.RetrievePreferencesUseCase
import com.chesire.nekome.app.settings.config.core.RetrieveUserUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultHomeScreenUseCase
import com.chesire.nekome.app.settings.config.core.UpdateDefaultSeriesStateUseCase
import com.chesire.nekome.app.settings.config.core.UpdateImageQualityUseCase
import com.chesire.nekome.app.settings.config.core.UpdateRateSeriesUseCase
import com.chesire.nekome.app.settings.config.core.UpdateThemeUseCase
import com.chesire.nekome.app.settings.config.core.UpdateTitleLanguageUseCase
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.Theme
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import com.chesire.nekome.datasource.user.User
import com.chesire.nekome.datasource.user.UserDomain
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
    private val retrieveUser = mockk<RetrieveUserUseCase>()
    private val updateRateSeries = mockk<UpdateRateSeriesUseCase>(relaxed = true)
    private val updateTheme = mockk<UpdateThemeUseCase>(relaxed = true)
    private val updateDefaultHomeScreen = mockk<UpdateDefaultHomeScreenUseCase>(relaxed = true)
    private val updateDefaultSeriesState = mockk<UpdateDefaultSeriesStateUseCase>(relaxed = true)
    private val updateImageQualityUseCase = mockk<UpdateImageQualityUseCase>(relaxed = true)
    private val updateTitleLanguage = mockk<UpdateTitleLanguageUseCase>(relaxed = true)
    private val logoutExecutor = mockk<LogoutExecutor>(relaxed = true)
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
                shouldRateSeries = false,
                imageQuality = ImageQuality.Low,
                titleLanguage = TitleLanguage.Canonical
            )
        )
        every {
            retrieveUser()
        } returns flowOf(
            User.Found(
                UserDomain(
                    userId = 0,
                    name = "Nekome",
                    avatar = ImageModel.empty,
                    coverImage = ImageModel.empty,
                    service = Service.Kitsu
                )
            )
        )

        viewModel = ConfigViewModel(
            retrievePreferences,
            retrieveUser,
            updateRateSeries,
            updateTheme,
            updateDefaultHomeScreen,
            updateDefaultSeriesState,
            updateImageQualityUseCase,
            updateTitleLanguage,
            logoutExecutor
        )
    }

    @Test
    fun `When initialize, uiState is expected defaults`() = runTest {
        val expected = UIState(
            userModel = UserModel(
                ImageModel.ImageData.empty.url,
                "Nekome"
            ),
            showLogoutDialog = false,
            executeLogout = null,
            themeValue = Theme.Dark,
            showThemeDialog = false,
            defaultHomeValue = HomeScreenOptions.Anime,
            showDefaultHomeDialog = false,
            defaultSeriesStatusValue = UserSeriesStatus.Current,
            showDefaultSeriesStatusDialog = false,
            imageQualityValue = ImageQuality.Low,
            showImageQualityDialog = false,
            titleLanguageValue = TitleLanguage.Canonical,
            showTitleLanguageDialog = false,
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

    @Test
    fun `When execute#OnImageQualityClicked, Then dialog is shown`() = runTest {
        viewModel.execute(ViewAction.OnImageQualityClicked)

        assertTrue(viewModel.uiState.value.showImageQualityDialog)
    }

    @Test
    fun `When execute#OnImageQualityChanged, Then dialog is hidden`() = runTest {
        viewModel.execute(ViewAction.OnImageQualityChanged(ImageQuality.Medium))

        assertFalse(viewModel.uiState.value.showImageQualityDialog)
    }

    @Test
    fun `When execute#OnImageQualityChanged, Then value is updated in use case`() = runTest {
        viewModel.execute(ViewAction.OnImageQualityChanged(ImageQuality.High))

        coVerify { updateImageQualityUseCase(ImageQuality.High) }
    }

    @Test
    fun `When execute#OnTitleLanguageClicked, Then dialog is shown`() = runTest {
        viewModel.execute(ViewAction.OnTitleLanguageClicked)

        assertTrue(viewModel.uiState.value.showTitleLanguageDialog)
    }

    @Test
    fun `When execute#OnTitleLanguageChanged, Then dialog is hidden`() = runTest {
        viewModel.execute(ViewAction.OnTitleLanguageChanged(TitleLanguage.Romaji))

        assertFalse(viewModel.uiState.value.showTitleLanguageDialog)
    }

    @Test
    fun `When execute#OnTitleLanguageChanged, Then value is updated in use case`() = runTest {
        viewModel.execute(ViewAction.OnTitleLanguageChanged(TitleLanguage.Romaji))

        coVerify { updateTitleLanguage(TitleLanguage.Romaji) }
    }
}
