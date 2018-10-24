package com.chesire.malime.core.flags

import org.junit.Assert.assertEquals
import org.junit.Test

class UserSeriesStatusTests {
    @Test
    fun `get status for unexpected Kitsu string of "unexpected", returns Unknown status`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getStatusForKitsuString("unexpected")
        )
    }

    @Test
    fun `get status for Kitsu string of "unknown", returns Unknown status`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getStatusForKitsuString("unknown")
        )
    }

    @Test
    fun `get status for Kitsu string of "current", returns Current status`() {
        assertEquals(
            UserSeriesStatus.Current,
            UserSeriesStatus.getStatusForKitsuString("current")
        )
    }

    @Test
    fun `get status for Kitsu string of "completed", returns Completed status`() {
        assertEquals(
            UserSeriesStatus.Completed,
            UserSeriesStatus.getStatusForKitsuString("completed")
        )
    }

    @Test
    fun `get status for Kitsu string of "on_hold", returns OnHold status`() {
        assertEquals(
            UserSeriesStatus.OnHold,
            UserSeriesStatus.getStatusForKitsuString("on_hold")
        )
    }

    @Test
    fun `get status for Kitsu string of "dropped", returns Dropped status`() {
        assertEquals(
            UserSeriesStatus.Dropped,
            UserSeriesStatus.getStatusForKitsuString("dropped")
        )
    }

    @Test
    fun `get status for Kitsu string of "planned", returns Planned status`() {
        assertEquals(
            UserSeriesStatus.Planned,
            UserSeriesStatus.getStatusForKitsuString("planned")
        )
    }

    @Test
    fun `get status for unexpected Mal id of 999, returns Unknown status`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getStatusForMalId(999)
        )
    }

    @Test
    fun `get status for Mal id of 0, returns Unknown status`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getStatusForMalId(0)
        )
    }

    @Test
    fun `get status for Mal id of 1, returns Current status`() {
        assertEquals(
            UserSeriesStatus.Current,
            UserSeriesStatus.getStatusForMalId(1)
        )
    }

    @Test
    fun `get status for Mal id of 2, returns Completed status`() {
        assertEquals(
            UserSeriesStatus.Completed,
            UserSeriesStatus.getStatusForMalId(2)
        )
    }

    @Test
    fun `get status for Mal id of 3, returns OnHold status`() {
        assertEquals(
            UserSeriesStatus.OnHold,
            UserSeriesStatus.getStatusForMalId(3)
        )
    }

    @Test
    fun `get status for Mal id of 4, returns Dropped status`() {
        assertEquals(
            UserSeriesStatus.Dropped,
            UserSeriesStatus.getStatusForMalId(4)
        )
    }

    @Test
    fun `get status for Mal id of 6, returns Planned status`() {
        assertEquals(
            UserSeriesStatus.Planned,
            UserSeriesStatus.getStatusForMalId(6)
        )
    }

    @Test
    fun `get status for unexpected id of 999, returns Unknown status`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getStatusForInternalId(999)
        )
    }

    @Test
    fun `get status for internal id of 0, returns Unknown status`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatus.getStatusForInternalId(0)
        )
    }

    @Test
    fun `get status for internal id of 1, returns Current status`() {
        assertEquals(
            UserSeriesStatus.Current,
            UserSeriesStatus.getStatusForInternalId(1)
        )
    }

    @Test
    fun `get status for internal id of 2, returns Completed status`() {
        assertEquals(
            UserSeriesStatus.Completed,
            UserSeriesStatus.getStatusForInternalId(2)
        )
    }

    @Test
    fun `get status for internal id of 3, returns OnHold status`() {
        assertEquals(
            UserSeriesStatus.OnHold,
            UserSeriesStatus.getStatusForInternalId(3)
        )
    }

    @Test
    fun `get status for internal id of 4, returns Dropped status`() {
        assertEquals(
            UserSeriesStatus.Dropped,
            UserSeriesStatus.getStatusForInternalId(4)
        )
    }

    @Test
    fun `get status for internal id of 5, returns Planned status`() {
        assertEquals(
            UserSeriesStatus.Planned,
            UserSeriesStatus.getStatusForInternalId(5)
        )
    }
}
