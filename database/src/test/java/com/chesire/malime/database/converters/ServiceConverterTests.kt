package com.chesire.malime.database.converters

import com.chesire.malime.core.flags.Service
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceConverterTests {
    @Test
    fun `fromService converts to enum name from Service`() {
        val converter = ServiceConverter()
        Service.values().forEach {
            assertEquals(it.name, converter.fromService(it))
        }
    }

    @Test
    fun `toService converts to Service from name`() {
        val converter = ServiceConverter()
        Service.values().forEach {
            assertEquals(it, converter.toService(it.name))
        }
    }
}
