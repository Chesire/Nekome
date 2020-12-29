package com.chesire.nekome.database.converters

import com.chesire.nekome.dataflags.SeriesStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class SeriesStatusConverterTests {
    @Test
    fun `fromSeriesStatus converts to enum name from SeriesStatus`() {
        val converter = SeriesStatusConverter()
        SeriesStatus.values().forEach {
            assertEquals(it.name, converter.fromSeriesStatus(it))
        }
    }

    @Test
    fun `toSeriesStatus converts to SeriesStatus from name`() {
        val converter = SeriesStatusConverter()
        SeriesStatus.values().forEach {
            assertEquals(it, converter.toSeriesStatus(it.name))
        }
    }
}
