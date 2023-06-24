package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BuildTitleUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var buildTitle: BuildTitleUseCase

    @Before
    fun setup() {
        clearAllMocks()

        buildTitle = BuildTitleUseCase(pref)
    }

    @Test
    fun `Given titleLanguage is canonical, When invoking, Then canon title is returned`() =
        runTest {
            val expected = "expectedTitle"
            every { pref.titleLanguage } returns flowOf(TitleLanguage.Canonical)
            val domain = createSeriesDomain(
                title = expected,
                otherTitles = mapOf("en" to "en")
            )

            val result = buildTitle(domain)

            assertEquals(expected, result)
        }

    @Test
    fun `Given titleLanguage is jp, When invoking, Then jp title is returned`() =
        runTest {
            val expected = "expectedTitle"
            every { pref.titleLanguage } returns flowOf(TitleLanguage.Japanese)
            val domain = createSeriesDomain(
                title = "incorrect",
                otherTitles = mapOf("jp" to expected)
            )

            val result = buildTitle(domain)

            assertEquals(expected, result)
        }

    @Test
    fun `Given titleLanguage is jp and no value, When invoking, Then fallback canonical title is returned`() =
        runTest {
            val expected = "expectedTitle"
            every { pref.titleLanguage } returns flowOf(TitleLanguage.Japanese)
            val domain = createSeriesDomain(
                title = expected,
                otherTitles = mapOf("jp" to "")
            )

            val result = buildTitle(domain)

            assertEquals(expected, result)
        }
}
