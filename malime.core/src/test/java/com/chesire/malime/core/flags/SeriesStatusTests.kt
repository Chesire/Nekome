package com.chesire.malime.core.flags

import org.junit.Assert.assertEquals
import org.junit.Test

class SeriesStatusTests {
    @Test
    fun `get status for Kitsu string of "unknown", returns Unknown status`() {
        assertEquals(SeriesStatus.Unknown, SeriesStatus.getStatusForKitsuString("unknown"))
    }

    @Test
    fun `get status for Kitsu string of "current", returns Current status`() {
        assertEquals(SeriesStatus.Current, SeriesStatus.getStatusForKitsuString("current"))
    }

    @Test
    fun `get status for Kitsu string of "finished", returns Finished status`() {
        assertEquals(SeriesStatus.Finished, SeriesStatus.getStatusForKitsuString("finished"))
    }

    @Test
    fun `get status for Kitsu string of "tba", returns TBA status`() {
        assertEquals(SeriesStatus.TBA, SeriesStatus.getStatusForKitsuString("tba"))
    }

    @Test
    fun `get status for Kitsu string of "unreleased", returns Unreleased status`() {
        assertEquals(
            SeriesStatus.Unreleased,
            SeriesStatus.getStatusForKitsuString("unreleased")
        )
    }

    @Test
    fun `get status for Kitsu string of "upcoming", returns Upcoming status`() {
        assertEquals(SeriesStatus.Upcoming, SeriesStatus.getStatusForKitsuString("upcoming"))
    }

    @Test
    fun `get status for unexpected Kitsu string of "unexpected", returns Unknown status`() {
        assertEquals(
            SeriesStatus.Unknown,
            SeriesStatus.getStatusForKitsuString("unexpected")
        )
    }

    @Test
    fun `get status for Mal id of 0, returns Unknown status`() {
        assertEquals(SeriesStatus.Unknown, SeriesStatus.getStatusForMalId(0))
    }

    @Test
    fun `get status for Mal id of 1, returns Current status`() {
        assertEquals(SeriesStatus.Current, SeriesStatus.getStatusForMalId(1))
    }

    @Test
    fun `get status for Mal id of 2, returns Finished status`() {
        assertEquals(SeriesStatus.Finished, SeriesStatus.getStatusForMalId(2))
    }

    @Test
    fun `get status for Mal id of 3, returns TBA status`() {
        assertEquals(SeriesStatus.TBA, SeriesStatus.getStatusForMalId(3))
    }

    @Test
    fun `get status for Mal id of 4, returns Unreleased status`() {
        assertEquals(SeriesStatus.Unreleased, SeriesStatus.getStatusForMalId(4))
    }

    @Test
    fun `get status for Mal id of 6, returns Upcoming status`() {
        assertEquals(SeriesStatus.Upcoming, SeriesStatus.getStatusForMalId(6))
    }

    @Test
    fun `get status for unexpected Mal id of 999, returns Unknown status`() {
        assertEquals(SeriesStatus.Unknown, SeriesStatus.getStatusForMalId(999))
    }

    @Test
    fun `get status for id of 0, returns Unknown status`() {
        assertEquals(SeriesStatus.Unknown, SeriesStatus.getStatusForInternalId(0))
    }

    @Test
    fun `get status for id of 1, returns Current status`() {
        assertEquals(SeriesStatus.Current, SeriesStatus.getStatusForInternalId(1))
    }

    @Test
    fun `get status for id of 2, returns Finished status`() {
        assertEquals(SeriesStatus.Finished, SeriesStatus.getStatusForInternalId(2))
    }

    @Test
    fun `get status for id of 3, returns TBA status`() {
        assertEquals(SeriesStatus.TBA, SeriesStatus.getStatusForInternalId(3))
    }

    @Test
    fun `get status for id of 4, returns Unreleased status`() {
        assertEquals(SeriesStatus.Unreleased, SeriesStatus.getStatusForInternalId(4))
    }

    @Test
    fun `get status for id of 5, returns Upcoming status`() {
        assertEquals(SeriesStatus.Upcoming, SeriesStatus.getStatusForInternalId(5))
    }

    @Test
    fun `get status for unexpected id of 999, returns Unknown status`() {
        assertEquals(SeriesStatus.Unknown, SeriesStatus.getStatusForInternalId(999))
    }
}