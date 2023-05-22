package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateDefaultHomeScreenUseCaseTest {

    private val pref = mockk<ApplicationPreferences>()
    private lateinit var updateDefaultHomeScreen: UpdateDefaultHomeScreenUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateDefaultHomeScreen = UpdateDefaultHomeScreenUseCase(pref)
    }

    @Test
    fun `When invoking, update pref with new value`() = runTest {
        coEvery { pref.updateDefaultHomeScreen(any()) } just runs

        updateDefaultHomeScreen(HomeScreenOptions.Manga)

        coEvery { pref.updateDefaultHomeScreen(HomeScreenOptions.Manga) }
    }
}
