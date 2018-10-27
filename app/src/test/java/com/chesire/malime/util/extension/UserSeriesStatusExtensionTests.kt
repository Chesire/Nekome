package com.chesire.malime.util.extension

import android.content.Context
import com.chesire.malime.R
import com.chesire.malime.core.flags.UserSeriesStatus
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Test

private const val COMPLETED = "Completed"
private const val CURRENT = "Current"
private const val DROPPED = "Dropped"
private const val ON_HOLD = "On-Hold"
private const val PLANNED = "Planned"
private const val UNKNOWN = "Unknown"

class UserSeriesStatusExtensionTests {
    private val mockContext = mock<Context> {
        on { getString(R.string.filter_state_completed) }.thenReturn(COMPLETED)
        on { getString(R.string.filter_state_current) }.thenReturn(CURRENT)
        on { getString(R.string.filter_state_dropped) }.thenReturn(DROPPED)
        on { getString(R.string.filter_state_on_hold) }.thenReturn(ON_HOLD)
        on { getString(R.string.filter_state_planned) }.thenReturn(PLANNED)
        on { getString(R.string.filter_state_unknown) }.thenReturn(UNKNOWN)
    }

    @Test
    fun `when UserSeriesStatus is 'Completed' expect correct string`() {
        Assert.assertEquals(COMPLETED, UserSeriesStatus.Completed.getString(mockContext))
    }

    @Test
    fun `when UserSeriesStatus is 'Current' expect correct string`() {
        Assert.assertEquals(CURRENT, UserSeriesStatus.Current.getString(mockContext))
    }

    @Test
    fun `when UserSeriesStatus is 'Dropped' expect correct string`() {
        Assert.assertEquals(DROPPED, UserSeriesStatus.Dropped.getString(mockContext))
    }

    @Test
    fun `when UserSeriesStatus is 'OnHold' expect correct string`() {
        Assert.assertEquals(ON_HOLD, UserSeriesStatus.OnHold.getString(mockContext))
    }

    @Test
    fun `when UserSeriesStatus is 'Planned' expect correct string`() {
        Assert.assertEquals(PLANNED, UserSeriesStatus.Planned.getString(mockContext))
    }

    @Test
    fun `when UserSeriesStatus is 'Unknown' expect correct string`() {
        Assert.assertEquals(UNKNOWN, UserSeriesStatus.Unknown.getString(mockContext))
    }
}
