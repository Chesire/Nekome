package com.chesire.malime.database.converters

import com.chesire.malime.core.models.ImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageModelConverterTests {
    private val jsonString =
        """{"large":{"height":7,"url":"largeUrl","width":6},"medium":{"height":5,"url":"mediumUrl","width":4},"small":{"height":3,"url":"smallUrl","width":2},"tiny":{"height":1,"url":"tinyUrl","width":0}}""".trimIndent()
    private val model = ImageModel(
        ImageModel.ImageData("tinyUrl", 0, 1),
        ImageModel.ImageData("smallUrl", 2, 3),
        ImageModel.ImageData("mediumUrl", 4, 5),
        ImageModel.ImageData("largeUrl", 6, 7)
    )

    @Test
    fun `fromImageModel converts to String`() {
        assertEquals(
            jsonString,
            com.chesire.malime.database.converters.ImageModelConverter().fromImageModel(model)
        )
    }

    @Test
    fun `toImageModel converts to ImageModel`() {
        assertEquals(
            model,
            com.chesire.malime.database.converters.ImageModelConverter().toImageModel(jsonString)
        )
    }
}
