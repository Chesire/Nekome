package com.chesire.nekome.core.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ImageModelTests {
    private val expected = ImageModel.ImageData("url", 0, 0)
    private val other = ImageModel.ImageData("", 0, 0)

    @Test
    fun `largest returns large when large is largest available model`() {
        assertEquals(expected, ImageModel(other, other, other, expected).largest)
    }

    @Test
    fun `largest returns medium when medium is largest available model`() {
        assertEquals(expected, ImageModel(other, other, expected, other).largest)
    }

    @Test
    fun `largest returns small when small is largest available model`() {
        assertEquals(expected, ImageModel(other, expected, other, other).largest)
    }

    @Test
    fun `largest returns tiny when tiny is largest available model`() {
        assertEquals(expected, ImageModel(expected, other, other, other).largest)
    }

    @Test
    fun `largest returns null when no available model`() {
        assertNull(ImageModel(other, other, other, other).largest)
    }

    @Test
    fun `middlest returns middle value for inputs`() {
        assertEquals(expected, ImageModel(other, other, expected, other).middlest)
    }

    @Test
    fun `smallest returns tiny when smallest available model`() {
        assertEquals(expected, ImageModel(expected, other, other, other).smallest)
    }

    @Test
    fun `smallest returns small when smallest available model`() {
        assertEquals(expected, ImageModel(other, expected, other, other).smallest)
    }

    @Test
    fun `smallest returns medium when smallest available model`() {
        assertEquals(expected, ImageModel(other, other, expected, other).smallest)
    }

    @Test
    fun `smallest returns large when smallest available model`() {
        assertEquals(expected, ImageModel(other, other, other, expected).smallest)
    }

    @Test
    fun `smallest returns null when no available model`() {
        assertNull(ImageModel(other, other, other, other).smallest)
    }

    @Test
    fun `empty gets a ImageModel with empty data`() {
        val empty = ImageModel.empty
        assertEquals(ImageModel.ImageData.empty, empty.large)
        assertEquals(ImageModel.ImageData.empty, empty.medium)
        assertEquals(ImageModel.ImageData.empty, empty.small)
        assertEquals(ImageModel.ImageData.empty, empty.tiny)
    }
}
