package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.Theme
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RetrievePreferencesUseCaseTest {

    private val applicationPref = mockk<ApplicationPreferences>()
    private val seriesPref = mockk<SeriesPreferences>()
    private lateinit var retrievePreferences: RetrievePreferencesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        retrievePreferences = RetrievePreferencesUseCase(applicationPref, seriesPref)
    }

    @Test
    fun `When invoking, populated PreferenceModel is returned in flow`() = runTest {
        val expected = PreferenceModel(
            theme = Theme.Light,
            defaultHomeScreen = HomeScreenOptions.Manga,
            defaultSeriesStatus = UserSeriesStatus.Dropped,
            shouldRateSeries = true,
            imageQuality = ImageQuality.High,
            titleLanguage = TitleLanguage.Canonical
        )
        coEvery { seriesPref.rateSeriesOnCompletion } returns flowOf(expected.shouldRateSeries)
        coEvery { seriesPref.imageQuality } returns flowOf(expected.imageQuality)
        coEvery { seriesPref.titleLanguage } returns flowOf(expected.titleLanguage)
        coEvery { applicationPref.theme } returns flowOf(expected.theme)
        coEvery { applicationPref.defaultHomeScreen } returns flowOf(expected.defaultHomeScreen)
        coEvery { applicationPref.defaultSeriesState } returns flowOf(expected.defaultSeriesStatus)

        val value = retrievePreferences().first()

        assertEquals(expected, value)
    }
}
