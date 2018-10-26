package com.chesire.malime.util.extension

import android.content.Context
import com.chesire.malime.R
import com.chesire.malime.core.flags.SeriesStatus
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Test

private const val CURRENT = "Current"
private const val FINISHED = "Finished"
private const val TBA = "TBA"
private const val UNRELEASED = "Unreleased"
private const val UPCOMING = "Upcoming"
private const val UNKNOWN = "Unknown"

class SeriesStatusExtensionTests {
    private val mockContext: Context = mock {
        on { getString(R.string.series_state_current) }.thenReturn(CURRENT)
        on { getString(R.string.series_state_finished) }.thenReturn(FINISHED)
        on { getString(R.string.series_state_tba) }.thenReturn(TBA)
        on { getString(R.string.series_state_unreleased) }.thenReturn(UNRELEASED)
        on { getString(R.string.series_state_upcoming) }.thenReturn(UPCOMING)
        on { getString(R.string.series_state_unknown) }.thenReturn(UNKNOWN)
    }

    @Test
    fun `when SeriesStatus is 'Current' expect correct string`() {
        Assert.assertEquals(CURRENT, SeriesStatus.Current.getString(mockContext))
    }

    @Test
    fun `when SeriesStatus is 'Finished' expect correct string`() {
        Assert.assertEquals(FINISHED, SeriesStatus.Finished.getString(mockContext))
    }

    @Test
    fun `when SeriesStatus is 'TBA' expect correct string`() {
        Assert.assertEquals(TBA, SeriesStatus.TBA.getString(mockContext))
    }

    @Test
    fun `when SeriesStatus is 'Unreleased' expect correct string`() {
        Assert.assertEquals(UNRELEASED, SeriesStatus.Unreleased.getString(mockContext))
    }

    @Test
    fun `when SeriesStatus is 'Upcoming' expect correct string`() {
        Assert.assertEquals(UPCOMING, SeriesStatus.Upcoming.getString(mockContext))
    }

    @Test
    fun `when SeriesStatus is 'Unknown' expect correct string`() {
        Assert.assertEquals(UNKNOWN, SeriesStatus.Unknown.getString(mockContext))
    }
}
