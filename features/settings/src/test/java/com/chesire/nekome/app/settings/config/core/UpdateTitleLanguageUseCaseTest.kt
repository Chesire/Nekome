package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateTitleLanguageUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var updateTitleLanguage: UpdateTitleLanguageUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateTitleLanguage = UpdateTitleLanguageUseCase(pref)
    }

    @Test
    fun `When invoking, update pref with new value`() = runTest {
        coEvery { pref.updateTitleLanguage(any()) } just runs

        updateTitleLanguage(TitleLanguage.Romaji)

        coEvery { pref.updateTitleLanguage(TitleLanguage.Romaji) }
    }
}
