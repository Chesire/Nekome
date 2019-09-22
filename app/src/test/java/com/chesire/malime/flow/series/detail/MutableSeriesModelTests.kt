package com.chesire.malime.flow.series.detail

import com.chesire.malime.createSeriesModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MutableSeriesModelTests {
    @Test
    fun `can generate MutableSeriesModel from SeriesModel`() {
        val originalModel = createSeriesModel(userId = 99)
        val testObject = MutableSeriesModel.from(originalModel)

        assertEquals(originalModel.userId, testObject.userSeriesId)
    }

    @Test
    fun `generated MutableSeriesModel seriesLength is "?" if unknown length`() {
        val originalModel = createSeriesModel(userId = 99, totalLength = 0)
        val testObject = MutableSeriesModel.from(originalModel)

        assertEquals("?", testObject.seriesLength)
    }

    @Test
    fun `generated MutableSeriesModel seriesLength is expected if known length`() {
        val originalModel = createSeriesModel(userId = 99, totalLength = 25)
        val testObject = MutableSeriesModel.from(originalModel)

        assertEquals("25", testObject.seriesLength)
    }
}
