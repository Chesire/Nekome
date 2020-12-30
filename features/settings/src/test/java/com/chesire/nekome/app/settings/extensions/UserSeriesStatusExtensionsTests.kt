package com.chesire.nekome.app.settings.extensions

import android.content.Context
import com.chesire.nekome.app.settings.R
import com.chesire.nekome.seriesflags.UserSeriesStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class UserSeriesStatusExtensionsTests {

    @Test
    fun `getValueMap returns expected map`() {
        val mockContext = mockk<Context> {
            every { getString(R.string.filter_by_current) } returns "current"
            every { getString(R.string.filter_by_completed) } returns "completed"
            every { getString(R.string.filter_by_on_hold) } returns "onhold"
            every { getString(R.string.filter_by_dropped) } returns "dropped"
            every { getString(R.string.filter_by_planned) } returns "planned"
        }
        val map = UserSeriesStatusExtensions.getValueMap(mockContext)

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
        val map = UserSeriesStatusExtensions.getValueMap(mockContext)

        assertFalse(UserSeriesStatus.values().count() == map.count())
    }
}
