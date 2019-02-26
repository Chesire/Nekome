package com.chesire.malime.db.converters

import com.chesire.malime.core.flags.Service
import org.junit.Assert.assertEquals
import org.junit.Test

class FlagConvertersTests {
    @Test
    fun `fromService converts to String`() {
        assertEquals("Kitsu", FlagConverters().fromService(Service.Kitsu))
    }

    @Test
    fun `toService converts to Service`() {
        assertEquals(Service.Kitsu, FlagConverters().toService("Kitsu"))
    }
}
