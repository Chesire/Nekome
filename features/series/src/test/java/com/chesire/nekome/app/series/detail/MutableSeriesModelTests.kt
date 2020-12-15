package com.chesire.nekome.app.series.detail

import com.chesire.nekome.testing.createSeriesDomain
import org.junit.Assert.assertEquals
import org.junit.Test

class MutableSeriesModelTests {
    @Test
    fun `can generate MutableSeriesModel from SeriesModel`() {
        val originalModel = createSeriesDomain(userId = 99)
        val classUnderTest = MutableSeriesModel.from(originalModel)

        assertEquals(originalModel.userId, classUnderTest.userSeriesId)
    }

    @Test
    fun `generated MutableSeriesModel seriesLength is "-" if unknown length`() {
        val originalModel = createSeriesDomain(userId = 99, totalLength = 0)
        val classUnderTest = MutableSeriesModel.from(originalModel)

        assertEquals("-", classUnderTest.seriesLength)
    }

    @Test
    fun `generated MutableSeriesModel seriesLength is expected if known length`() {
        val originalModel = createSeriesDomain(userId = 99, totalLength = 25)
        val classUnderTest = MutableSeriesModel.from(originalModel)

        assertEquals("25", classUnderTest.seriesLength)
    }
}
