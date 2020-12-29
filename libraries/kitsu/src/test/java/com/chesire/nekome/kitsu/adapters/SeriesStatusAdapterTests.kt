package com.chesire.nekome.kitsu.adapters

import com.chesire.nekome.seriesflags.SeriesStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class SeriesStatusAdapterTests {
    @Test
    fun `seriesStatusFromString with 'current'`() {
        assertEquals(
            SeriesStatus.Current,
            SeriesStatusAdapter().seriesStatusFromString("current")
        )
    }

    @Test
    fun `seriesStatusFromString with 'finished'`() {
        assertEquals(
            SeriesStatus.Finished,
            SeriesStatusAdapter().seriesStatusFromString("finished")
        )
    }

    @Test
    fun `seriesStatusFromString with 'tba'`() {
        assertEquals(
            SeriesStatus.TBA,
            SeriesStatusAdapter().seriesStatusFromString("tba")
        )
    }

    @Test
    fun `seriesStatusFromString with 'unreleased'`() {
        assertEquals(
            SeriesStatus.Unreleased,
            SeriesStatusAdapter().seriesStatusFromString("unreleased")
        )
    }

    @Test
    fun `seriesStatusFromString with 'upcoming'`() {
        assertEquals(
            SeriesStatus.Upcoming,
            SeriesStatusAdapter().seriesStatusFromString("upcoming")
        )
    }

    @Test
    fun `seriesStatusFromString with 'unknown'`() {
        assertEquals(
            SeriesStatus.Unknown,
            SeriesStatusAdapter().seriesStatusFromString("unknown")
        )
    }

    @Test
    fun `seriesStatusToString with 'SeriesStatus#Current'`() {
        assertEquals(
            "current",
            SeriesStatusAdapter().seriesStatusToString(SeriesStatus.Current)
        )
    }

    @Test
    fun `seriesStatusToString with 'SeriesStatus#Finished'`() {
        assertEquals(
            "finished",
            SeriesStatusAdapter().seriesStatusToString(SeriesStatus.Finished)
        )
    }

    @Test
    fun `seriesStatusToString with 'SeriesStatus#TBA'`() {
        assertEquals(
            "tba",
            SeriesStatusAdapter().seriesStatusToString(SeriesStatus.TBA)
        )
    }

    @Test
    fun `seriesStatusToString with 'SeriesStatus#Unreleased'`() {
        assertEquals(
            "unreleased",
            SeriesStatusAdapter().seriesStatusToString(SeriesStatus.Unreleased)
        )
    }

    @Test
    fun `seriesStatusToString with 'SeriesStatus#Upcoming'`() {
        assertEquals(
            "upcoming",
            SeriesStatusAdapter().seriesStatusToString(SeriesStatus.Upcoming)
        )
    }

    @Test
    fun `seriesStatusToString with 'SeriesStatus#Unknown'`() {
        assertEquals(
            "unknown",
            SeriesStatusAdapter().seriesStatusToString(SeriesStatus.Unknown)
        )
    }
}
