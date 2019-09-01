package com.chesire.malime.database.converters

import com.chesire.malime.core.flags.Subtype
import org.junit.Assert.assertEquals
import org.junit.Test

class SubtypeConverterTests {
    @Test
    fun `fromSubtype converts to enum name from Subtype`() {
        val converter = SubtypeConverter()
        Subtype.values().forEach {
            assertEquals(it.name, converter.fromSubtype(it))
        }
    }

    @Test
    fun `toSubtype converts to Subtype from name`() {
        val converter = SubtypeConverter()
        Subtype.values().forEach {
            assertEquals(it, converter.toSubtype(it.name))
        }
    }
}
