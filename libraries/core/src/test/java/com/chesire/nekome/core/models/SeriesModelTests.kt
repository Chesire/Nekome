package com.chesire.nekome.core.models

import com.chesire.nekome.testing.createSeriesModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeriesModelTests {
    @Test
    fun `lengthKnown is true if length is not 0`() {
        val testObject = createSeriesModel(totalLength = 1)

        assertTrue(testObject.lengthKnown)
    }

    @Test
    fun `lengthKnown is false if length is 0`() {
        val testObject = createSeriesModel(totalLength = 0)

        assertFalse(testObject.lengthKnown)
    }

    @Test
    fun `areItemsTheSame is true if matching ids`() {
        val model1 = createSeriesModel(id = 1)
        val model2 = createSeriesModel(id = 1)
        val model3 = createSeriesModel(id = 0)
        val testObject = SeriesModel.DiffCallback()

        assertTrue(testObject.areItemsTheSame(model1, model2))
        assertFalse(testObject.areItemsTheSame(model1, model3))
    }

    @Test
    fun `areContents the same is true if items have matching isEquals`() {
        val model1 = createSeriesModel(totalLength = 1)
        val model2 = createSeriesModel(totalLength = 1)
        val model3 = createSeriesModel(totalLength = 0)
        val testObject = SeriesModel.DiffCallback()

        assertTrue(testObject.areContentsTheSame(model1, model2))
        assertFalse(testObject.areContentsTheSame(model1, model3))
    }
}
