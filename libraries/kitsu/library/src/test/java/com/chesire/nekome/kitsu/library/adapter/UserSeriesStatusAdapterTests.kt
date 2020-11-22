package com.chesire.nekome.kitsu.library.adapter

import com.chesire.nekome.core.flags.UserSeriesStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class UserSeriesStatusAdapterTests {

    private val adapter = UserSeriesStatusAdapter()

    @Test
    fun `userSeriesStatusFromString with 'current'`() {
        assertEquals(UserSeriesStatus.Current, adapter.userSeriesStatusFromString("current"))
    }

    @Test
    fun `userSeriesStatusFromString with 'completed'`() {
        assertEquals(UserSeriesStatus.Completed, adapter.userSeriesStatusFromString("completed"))
    }

    @Test
    fun `userSeriesStatusFromString with 'on_hold'`() {
        assertEquals(UserSeriesStatus.OnHold, adapter.userSeriesStatusFromString("on_hold"))
    }

    @Test
    fun `userSeriesStatusFromString with 'dropped'`() {
        assertEquals(UserSeriesStatus.Dropped, adapter.userSeriesStatusFromString("dropped"))
    }

    @Test
    fun `userSeriesStatusFromString with 'planned'`() {
        assertEquals(UserSeriesStatus.Planned, adapter.userSeriesStatusFromString("planned"))
    }

    @Test
    fun `userSeriesStatusFromString with 'unknown'`() {
        assertEquals(UserSeriesStatus.Unknown, adapter.userSeriesStatusFromString("unknown"))
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Current`() {
        assertEquals("current", adapter.userSeriesStatusToString(UserSeriesStatus.Current))
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Completed`() {
        assertEquals("completed", adapter.userSeriesStatusToString(UserSeriesStatus.Completed))
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#OnHold`() {
        assertEquals("on_hold", adapter.userSeriesStatusToString(UserSeriesStatus.OnHold))
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Dropped`() {
        assertEquals("dropped", adapter.userSeriesStatusToString(UserSeriesStatus.Dropped))
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Planned`() {
        assertEquals("planned", adapter.userSeriesStatusToString(UserSeriesStatus.Planned))
    }

    @Test
    fun `userSeriesStatusToString with 'UserSeriesStatus#Unknown`() {
        assertEquals("unknown", adapter.userSeriesStatusToString(UserSeriesStatus.Unknown))
    }
}
