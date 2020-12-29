package com.chesire.nekome.database.converters

import com.chesire.nekome.seriesflags.UserSeriesStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class UserSeriesStatusConverterTests {
    @Test
    fun `fromUserSeriesStatus converts to enum name from UserSeriesStatus`() {
        val converter = UserSeriesStatusConverter()
        UserSeriesStatus.values().forEach {
            assertEquals(it.name, converter.fromUserSeriesStatus(it))
        }
    }

    @Test
    fun `toUserSeriesStatus converts to UserSeriesStatus from name`() {
        val converter = UserSeriesStatusConverter()
        UserSeriesStatus.values().forEach {
            assertEquals(it, converter.toUserSeriesStatus(it.name))
        }
    }
}
