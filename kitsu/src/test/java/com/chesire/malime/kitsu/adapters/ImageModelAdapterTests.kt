package com.chesire.malime.kitsu.adapters

import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.api.intermediaries.ParsingImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageModelAdapterTests {
    @Test
    fun `imageFromParsingImage returns with populated fields`() {
        val dimensionsMeta = ParsingImageModel.ImageMeta.DimensionsMeta(
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(1, 2),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(3, 4),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(5, 6),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(7, 8)
        )
        val meta = ParsingImageModel.ImageMeta(dimensionsMeta)
        val input = ParsingImageModel("tiny", "small", "medium", "large", meta)

        val classUnderTest = ImageModelAdapter()
        val result = classUnderTest.imageFromParsingImage(input)

        assertEquals(1, result.tiny.width)
        assertEquals(2, result.tiny.height)
        assertEquals("tiny", result.tiny.url)
        assertEquals(3, result.small.width)
        assertEquals(4, result.small.height)
        assertEquals("small", result.small.url)
        assertEquals(5, result.medium.width)
        assertEquals(6, result.medium.height)
        assertEquals("medium", result.medium.url)
        assertEquals(7, result.large.width)
        assertEquals(8, result.large.height)
        assertEquals("large", result.large.url)
    }

    @Test
    fun `fields missing the sizeUrl are returned empty`() {
        val dimensionsMeta = ParsingImageModel.ImageMeta.DimensionsMeta(
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(1, 2),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(3, 4),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(5, 6),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(7, 8)
        )
        val meta = ParsingImageModel.ImageMeta(dimensionsMeta)
        val input = ParsingImageModel("", "small", "", "large", meta)

        val classUnderTest = ImageModelAdapter()
        val result = classUnderTest.imageFromParsingImage(input)

        assertEquals(0, result.tiny.width)
        assertEquals(0, result.tiny.height)
        assertEquals("", result.tiny.url)
        assertEquals(3, result.small.width)
        assertEquals(4, result.small.height)
        assertEquals("small", result.small.url)
        assertEquals(0, result.medium.width)
        assertEquals(0, result.medium.height)
        assertEquals("", result.medium.url)
        assertEquals(7, result.large.width)
        assertEquals(8, result.large.height)
        assertEquals("large", result.large.url)
    }

    @Test
    fun `null dimensionsData returns 0 on width and height`() {
        val dimensionsMeta = ParsingImageModel.ImageMeta.DimensionsMeta(
            null,
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(3, 4),
            null,
            null
        )
        val meta = ParsingImageModel.ImageMeta(dimensionsMeta)
        val input = ParsingImageModel("tiny", "small", "medium", "large", meta)

        val classUnderTest = ImageModelAdapter()
        val result = classUnderTest.imageFromParsingImage(input)

        assertEquals(0, result.tiny.width)
        assertEquals(0, result.tiny.height)
        assertEquals("tiny", result.tiny.url)
        assertEquals(3, result.small.width)
        assertEquals(4, result.small.height)
        assertEquals("small", result.small.url)
        assertEquals(0, result.medium.width)
        assertEquals(0, result.medium.height)
        assertEquals("medium", result.medium.url)
        assertEquals(0, result.large.width)
        assertEquals(0, result.large.height)
        assertEquals("large", result.large.url)
    }

    @Test
    fun `null width returns 0 on width`() {
        val dimensionsMeta = ParsingImageModel.ImageMeta.DimensionsMeta(
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(null, 2),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(null, 4),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(null, 6),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(7, 8)
        )
        val meta = ParsingImageModel.ImageMeta(dimensionsMeta)
        val input = ParsingImageModel("tiny", "small", "medium", "large", meta)

        val classUnderTest = ImageModelAdapter()
        val result = classUnderTest.imageFromParsingImage(input)

        assertEquals(0, result.tiny.width)
        assertEquals(2, result.tiny.height)
        assertEquals("tiny", result.tiny.url)
        assertEquals(0, result.small.width)
        assertEquals(4, result.small.height)
        assertEquals("small", result.small.url)
        assertEquals(0, result.medium.width)
        assertEquals(6, result.medium.height)
        assertEquals("medium", result.medium.url)
        assertEquals(7, result.large.width)
        assertEquals(8, result.large.height)
        assertEquals("large", result.large.url)
    }

    @Test
    fun `null height returns 0 on height`() {
        val dimensionsMeta = ParsingImageModel.ImageMeta.DimensionsMeta(
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(1, null),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(3, null),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(5, null),
            ParsingImageModel.ImageMeta.DimensionsMeta.DimensionsData(7, 8)
        )
        val meta = ParsingImageModel.ImageMeta(dimensionsMeta)
        val input = ParsingImageModel("tiny", "small", "medium", "large", meta)

        val classUnderTest = ImageModelAdapter()
        val result = classUnderTest.imageFromParsingImage(input)

        assertEquals(1, result.tiny.width)
        assertEquals(0, result.tiny.height)
        assertEquals("tiny", result.tiny.url)
        assertEquals(3, result.small.width)
        assertEquals(0, result.small.height)
        assertEquals("small", result.small.url)
        assertEquals(5, result.medium.width)
        assertEquals(0, result.medium.height)
        assertEquals("medium", result.medium.url)
        assertEquals(7, result.large.width)
        assertEquals(8, result.large.height)
        assertEquals("large", result.large.url)
    }
}
