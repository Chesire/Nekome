package com.chesire.nekome.app.series.list

import com.chesire.nekome.testing.createSeriesDomain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeriesDomainExtensionsTests {

    @Test
    fun `lengthKnown, with totalLength of 0, returns false`() {
        val seriesDomain = createSeriesDomain(totalLength = 0)
        assertFalse(seriesDomain.lengthKnown)
    }

    @Test
    fun `lengthKnown, with totalLength of 1, returns true`() {
        val seriesDomain = createSeriesDomain(totalLength = 1)
        assertTrue(seriesDomain.lengthKnown)
    }
}
