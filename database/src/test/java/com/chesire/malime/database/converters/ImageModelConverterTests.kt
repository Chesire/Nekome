package com.chesire.malime.database.converters

import com.chesire.malime.core.models.ImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageModelConverterTests {
    private val jsonString =
        """{"tiny":{"url":"tinyUrl","width":0,"height":1},"small":{"url":"smallUrl","width":2,"height":3},"medium":{"url":"mediumUrl","width":4,"height":5},"large":{"url":"largeUrl","width":6,"height":7}}""".trimIndent()
    private val model = ImageModel(
        ImageModel.ImageData("tinyUrl", 0, 1),
        ImageModel.ImageData("smallUrl", 2, 3),
        ImageModel.ImageData("mediumUrl", 4, 5),
        ImageModel.ImageData("largeUrl", 6, 7)
    )

    @Test
    fun `fromImageModel converts to String`() {
        assertEquals(jsonString, ImageModelConverter().fromImageModel(model))
    }

    @Test
    fun `toImageModel converts to ImageModel`() {
        assertEquals(model, ImageModelConverter().toImageModel(jsonString))
    }
}
