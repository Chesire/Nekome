@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.flags.Theme
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateThemeUseCaseTest {

    private val pref = mockk<ApplicationPreferences>()
    private lateinit var updateTheme: UpdateThemeUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateTheme = UpdateThemeUseCase(pref)
    }

    @Test
    fun `When invoking, update pref with new value`() = runTest {
        coEvery { pref.updateTheme(any()) } just runs

        updateTheme(Theme.Light)

        coEvery { pref.updateTheme(Theme.Light) }
    }
}
