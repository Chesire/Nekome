package com.chesire.nekome.core.models

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeriesModelTests {
    @Test
    fun `lengthKnown is true if length is not 0`() {
        val testObject = createSeriesModel(newLength = 1)

        assertTrue(testObject.lengthKnown)
    }

    @Test
    fun `lengthKnown is false if length is 0`() {
        val testObject = createSeriesModel(newLength = 0)

        assertFalse(testObject.lengthKnown)
    }

    @Test
    fun `areItemsTheSame is true if matching ids`() {
        val model1 = createSeriesModel(newId = 1)
        val model2 = createSeriesModel(newId = 1)
        val model3 = createSeriesModel(newId = 0)
        val testObject = SeriesModel.DiffCallback()

        assertTrue(testObject.areItemsTheSame(model1, model2))
        assertFalse(testObject.areItemsTheSame(model1, model3))
    }

    @Test
    fun `areContents the same is true if items have matching isEquals`() {
        val model1 = createSeriesModel(newLength = 1)
        val model2 = createSeriesModel(newLength = 1)
        val model3 = createSeriesModel(newLength = 0)
        val testObject = SeriesModel.DiffCallback()

        assertTrue(testObject.areContentsTheSame(model1, model2))
        assertFalse(testObject.areContentsTheSame(model1, model3))
    }

    private fun createSeriesModel(newId: Int = 0, newLength: Int = 0) = SeriesModel(
        id = newId,
        userId = 0,
        type = SeriesType.Unknown,
        subtype = Subtype.Unknown,
        slug = "",
        synopsis = "",
        title = "",
        seriesStatus = SeriesStatus.Unknown,
        userSeriesStatus = UserSeriesStatus.Unknown,
        progress = 0,
        totalLength = newLength,
        posterImage = ImageModel.empty,
        coverImage = ImageModel.empty,
        nsfw = false,
        startDate = "",
        endDate = ""
    )
}
