package com.chesire.nekome.dataflags

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class UserSeriesStatusTests {
    @Test
    fun `getValueMap returns expected map`() {
        val mockContext = mockk<Context> {
            every { getString(R.string.filter_by_current) } returns "current"
            every { getString(R.string.filter_by_completed) } returns "completed"
            every { getString(R.string.filter_by_on_hold) } returns "onhold"
            every { getString(R.string.filter_by_dropped) } returns "dropped"
            every { getString(R.string.filter_by_planned) } returns "planned"
        }
        val map = UserSeriesStatus.getValueMap(mockContext)

        assertEquals("current", map.getValue(0))
        assertEquals("completed", map.getValue(1))
        assertEquals("onhold", map.getValue(2))
        assertEquals("dropped", map.getValue(3))
        assertEquals("planned", map.getValue(4))
    }

    @Test
    fun `getValueMap ignores the Unknown value`() {
        val mockContext = mockk<Context> {
            every { getString(R.string.filter_by_current) } returns "current"
            every { getString(R.string.filter_by_completed) } returns "completed"
            every { getString(R.string.filter_by_on_hold) } returns "onhold"
            every { getString(R.string.filter_by_dropped) } returns "dropped"
            every { getString(R.string.filter_by_planned) } returns "planned"
        }
        val map = UserSeriesStatus.getValueMap(mockContext)

        assertFalse(UserSeriesStatus.values().count() == map.count())
    }

    @Test
    fun `getFromIndex with non int value returns Unknown value`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getFromIndex("not an int")
        )
    }

    @Test
    fun `getFromIndex with out of range value returns Unknown value`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getFromIndex("99")
        )
    }

    @Test
    fun `getFromIndex with '-1' value returns Unknown value`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getFromIndex("-1")
        )
    }

    @Test
    fun `getFromIndex with '0' value returns Current value`() {
        assertEquals(
            UserSeriesStatus.Current,
            UserSeriesStatus.getFromIndex("0")
        )
    }

    @Test
    fun `getFromIndex with '1' value returns Completed value`() {
        assertEquals(
            UserSeriesStatus.Completed,
            UserSeriesStatus.getFromIndex("1")
        )
    }

    @Test
    fun `getFromIndex with '2' value returns OnHold value`() {
        assertEquals(
            UserSeriesStatus.OnHold,
            UserSeriesStatus.getFromIndex("2")
        )
    }

    @Test
    fun `getFromIndex with '3' value returns Dropped value`() {
        assertEquals(
            UserSeriesStatus.Dropped,
            UserSeriesStatus.getFromIndex("3")
        )
    }

    @Test
    fun `getFromIndex with '4' value returns Planned value`() {
        assertEquals(
            UserSeriesStatus.Planned,
            UserSeriesStatus.getFromIndex("4")
        )
    }
}
