package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.UserSeriesStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class UserSeriesStatusAdapterTests {
    @Test
    fun `userSeriesStatusFromString with 'current'`() {
        assertEquals(
            UserSeriesStatus.Current,
            UserSeriesStatusAdapter().userSeriesStatusFromString("current")
        )
    }

    @Test
    fun `userSeriesStatusFromString with 'completed'`() {
        assertEquals(
            UserSeriesStatus.Completed,
            UserSeriesStatusAdapter().userSeriesStatusFromString("completed")
        )
    }

    @Test
    fun `userSeriesStatusFromString with 'on_hold'`() {
        assertEquals(
            UserSeriesStatus.OnHold,
            UserSeriesStatusAdapter().userSeriesStatusFromString("on_hold")
        )
    }

    @Test
    fun `userSeriesStatusFromString with 'dropped'`() {
        assertEquals(
            UserSeriesStatus.Dropped,
            UserSeriesStatusAdapter().userSeriesStatusFromString("dropped")
        )
    }

    @Test
    fun `userSeriesStatusFromString with 'planned'`() {
        assertEquals(
            UserSeriesStatus.Planned,
            UserSeriesStatusAdapter().userSeriesStatusFromString("planned")
        )
    }

    @Test
    fun `userSeriesStatusFromString with 'unknown'`() {
        assertEquals(
            UserSeriesStatus.Unknown,
            UserSeriesStatusAdapter().userSeriesStatusFromString("unknown")
        )
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Current`() {
        assertEquals(
            "current",
            UserSeriesStatusAdapter().userSeriesStatusToString(UserSeriesStatus.Current)
        )
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Completed`() {
        assertEquals(
            "completed",
            UserSeriesStatusAdapter().userSeriesStatusToString(UserSeriesStatus.Completed)
        )
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#OnHold`() {
        assertEquals(
            "on_hold",
            UserSeriesStatusAdapter().userSeriesStatusToString(UserSeriesStatus.OnHold)
        )
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Dropped`() {
        assertEquals(
            "dropped",
            UserSeriesStatusAdapter().userSeriesStatusToString(UserSeriesStatus.Dropped)
        )
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Planned`() {
        assertEquals(
            "planned",
            UserSeriesStatusAdapter().userSeriesStatusToString(UserSeriesStatus.Planned)
        )
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Unknown`() {
        assertEquals(
            "unknown",
            UserSeriesStatusAdapter().userSeriesStatusToString(UserSeriesStatus.Unknown)
        )
    }
}
