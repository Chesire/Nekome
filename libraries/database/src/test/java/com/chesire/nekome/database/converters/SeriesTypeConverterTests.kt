package com.chesire.nekome.database.converters

import com.chesire.nekome.core.flags.SeriesType
import org.junit.Assert.assertEquals
import org.junit.Test

class SeriesTypeConverterTests {
    @Test
    fun `fromSeriesType converts to enum name from SeriesType`() {
        val converter = SeriesTypeConverter()
        SeriesType.values().forEach {
            assertEquals(it.name, converter.fromSeriesType(it))
        }
    }

    @Test
    fun `toSeriesType converts to SeriesType from name`() {
        val converter = SeriesTypeConverter()
        SeriesType.values().forEach {
            assertEquals(it, converter.toSeriesType(it.name))
        }
    }
}
