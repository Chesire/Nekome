package com.chesire.nekome.core.preferences.flags

import org.junit.Assert.assertEquals
import org.junit.Test

class ImageQualityTests {

    @Test
    fun `forIndex ImageQuality#Low returns expected value`() {
        assertEquals(
            ImageQuality.Low,
            ImageQuality.forIndex(0)
        )
    }

    @Test
    fun `forIndex ImageQuality#Medium returns expected value`() {
        assertEquals(
            ImageQuality.Medium,
            ImageQuality.forIndex(1)
        )
    }

    @Test
    fun `forIndex ImageQuality#High returns expected value`() {
        assertEquals(
            ImageQuality.High,
            ImageQuality.forIndex(2)
        )
    }
}
