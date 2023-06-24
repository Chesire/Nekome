package com.chesire.nekome.database.converters

import org.junit.Assert.assertEquals
import org.junit.Test

class MapConverterTests {

    @Test
    fun `fromMap converts to String from Map`() {
        val input = mapOf(
            "en" to "enValue",
            "jp" to "jpValue"
        )
        val expected = """{"en":"enValue","jp":"jpValue"}"""

        val result = MapConverter().fromMap(input)

        assertEquals(expected, result)
    }

    @Test
    fun `toMap converts to Map from String`() {
        val input = """{"en":"enValue","jp":"jpValue"}"""
        val expected = mapOf(
            "en" to "enValue",
            "jp" to "jpValue"
        )

        val result = MapConverter().toMap(input)

        assertEquals(expected, result)
    }

    @Test
    fun `toMap converts to empty Map from empty String`() {
        val input = """"""
        val expected = emptyMap<String, String>()

        val result = MapConverter().toMap(input)

        assertEquals(expected, result)
    }
}
