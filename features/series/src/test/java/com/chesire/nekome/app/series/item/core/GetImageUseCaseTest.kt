package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.testing.createImageModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetImageUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var getImage: GetImageUseCase

    private val model = createImageModel(
        tiny = ImageModel.ImageData(
            url = "tiny",
            width = 0,
            height = 0
        ),
        small = ImageModel.ImageData(
            url = "small",
            width = 0,
            height = 0
        ),
        medium = ImageModel.ImageData(
            url = "medium",
            width = 0,
            height = 0
        ),
        large = ImageModel.ImageData(
            url = "large",
            width = 0,
            height = 0
        )
    )

    @Before
    fun setup() {
        clearAllMocks()

        getImage = GetImageUseCase(pref)
    }

    @Test
    fun `Given imageQuality is Low, When invoking, Then smallest image is returned`() = runTest {
        coEvery { pref.imageQuality } returns flowOf(ImageQuality.Low)

        val result = getImage(model)

        assertEquals(model.tiny.url, result)
    }

    @Test
    fun `Given imageQuality is Medium, When invoking, Then medium image is returned`() = runTest {
        coEvery { pref.imageQuality } returns flowOf(ImageQuality.Medium)

        val result = getImage(model)

        assertEquals(model.medium.url, result)

    }

    @Test
    fun `Given imageQuality is High, When invoking, Then biggest image is returned`() = runTest {
        coEvery { pref.imageQuality } returns flowOf(ImageQuality.High)

        val result = getImage(model)

        assertEquals(model.large.url, result)
    }
}
