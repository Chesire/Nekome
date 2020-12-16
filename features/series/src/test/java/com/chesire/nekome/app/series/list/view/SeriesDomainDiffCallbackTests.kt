package com.chesire.nekome.app.series.list.view

import com.chesire.nekome.testing.createSeriesDomain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeriesDomainDiffCallbackTests {

    private val diff = SeriesDomainDiffCallback()

    @Test
    fun `areItemsTheSame, with matching ids, returns true`() {
        val item1 = createSeriesDomain(id = 1)
        val item2 = createSeriesDomain(id = 1)

        val result = diff.areItemsTheSame(item1, item2)

        assertTrue(result)
    }

    @Test
    fun `areItemsTheSame, with different ids, returns false`() {
        val item1 = createSeriesDomain(id = 1)
        val item2 = createSeriesDomain(id = 2)

        val result = diff.areItemsTheSame(item1, item2)

        assertFalse(result)
    }

    @Test
    fun `areContentsTheSame, with same objects, returns true`() {
        val item1 = createSeriesDomain(id = 1)
        val item2 = item1

        val result = diff.areContentsTheSame(item1, item2)

        assertTrue(result)
    }

    @Test
    fun `areContentsTheSame, with different objects, returns false`() {
        val item1 = createSeriesDomain(id = 1)
        val item2 = createSeriesDomain(id = 2)

        val result = diff.areContentsTheSame(item1, item2)

        assertFalse(result)
    }
}
