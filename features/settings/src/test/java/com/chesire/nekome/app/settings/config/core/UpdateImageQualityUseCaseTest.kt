package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.ImageQuality
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateImageQualityUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var updateImageQuality: UpdateImageQualityUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateImageQuality = UpdateImageQualityUseCase(pref)
    }

    @Test
    fun `When invoking, update pref with new value`() = runTest {
        coEvery { pref.updateImageQuality(any()) } just runs

        updateImageQuality(ImageQuality.Medium)

        coEvery { pref.updateImageQuality(ImageQuality.Medium) }
    }
}
