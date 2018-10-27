package com.chesire.malime.core.room

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import org.junit.Assert
import org.junit.Test

class ConvertersTests {
    private val converters = Converters()

    @Test
    fun `fromItemType to id`() {
        Assert.assertEquals(0, converters.fromItemType(ItemType.Unknown))
        Assert.assertEquals(1, converters.fromItemType(ItemType.Anime))
        Assert.assertEquals(2, converters.fromItemType(ItemType.Manga))
    }

    @Test
    fun `toItemType from id`() {
        Assert.assertEquals(ItemType.Unknown, converters.toItemType(0))
        Assert.assertEquals(ItemType.Anime, converters.toItemType(1))
        Assert.assertEquals(ItemType.Manga, converters.toItemType(2))
    }

    @Test
    fun `fromSubType to id`() {
        Assert.assertEquals(0, converters.fromSubtype(Subtype.Unknown))
        Assert.assertEquals(1, converters.fromSubtype(Subtype.ONA))
        Assert.assertEquals(2, converters.fromSubtype(Subtype.OVA))
        Assert.assertEquals(3, converters.fromSubtype(Subtype.TV))
        Assert.assertEquals(4, converters.fromSubtype(Subtype.Movie))
        Assert.assertEquals(5, converters.fromSubtype(Subtype.Music))
        Assert.assertEquals(6, converters.fromSubtype(Subtype.Special))
        Assert.assertEquals(7, converters.fromSubtype(Subtype.Doujin))
        Assert.assertEquals(8, converters.fromSubtype(Subtype.Manga))
        Assert.assertEquals(9, converters.fromSubtype(Subtype.Manhua))
        Assert.assertEquals(10, converters.fromSubtype(Subtype.Manhwa))
        Assert.assertEquals(11, converters.fromSubtype(Subtype.Novel))
        Assert.assertEquals(12, converters.fromSubtype(Subtype.OEL))
        Assert.assertEquals(13, converters.fromSubtype(Subtype.Oneshot))
    }

    @Test
    fun `toSubType from id`() {
        Assert.assertEquals(Subtype.Unknown, converters.toSubtype(0))
        Assert.assertEquals(Subtype.ONA, converters.toSubtype(1))
        Assert.assertEquals(Subtype.OVA, converters.toSubtype(2))
        Assert.assertEquals(Subtype.TV, converters.toSubtype(3))
        Assert.assertEquals(Subtype.Movie, converters.toSubtype(4))
        Assert.assertEquals(Subtype.Music, converters.toSubtype(5))
        Assert.assertEquals(Subtype.Special, converters.toSubtype(6))
        Assert.assertEquals(Subtype.Doujin, converters.toSubtype(7))
        Assert.assertEquals(Subtype.Manga, converters.toSubtype(8))
        Assert.assertEquals(Subtype.Manhua, converters.toSubtype(9))
        Assert.assertEquals(Subtype.Manhwa, converters.toSubtype(10))
        Assert.assertEquals(Subtype.Novel, converters.toSubtype(11))
        Assert.assertEquals(Subtype.OEL, converters.toSubtype(12))
        Assert.assertEquals(Subtype.Oneshot, converters.toSubtype(13))
    }

    @Test
    fun `fromSeriesStatus to id`() {
        Assert.assertEquals(0, converters.fromSeriesStatus(SeriesStatus.Unknown))
        Assert.assertEquals(1, converters.fromSeriesStatus(SeriesStatus.Current))
        Assert.assertEquals(2, converters.fromSeriesStatus(SeriesStatus.Finished))
        Assert.assertEquals(3, converters.fromSeriesStatus(SeriesStatus.TBA))
        Assert.assertEquals(4, converters.fromSeriesStatus(SeriesStatus.Unreleased))
        Assert.assertEquals(5, converters.fromSeriesStatus(SeriesStatus.Upcoming))
    }

    @Test
    fun `toSeriesStatus from id`() {
        Assert.assertEquals(SeriesStatus.Unknown, converters.toSeriesStatus(0))
        Assert.assertEquals(SeriesStatus.Current, converters.toSeriesStatus(1))
        Assert.assertEquals(SeriesStatus.Finished, converters.toSeriesStatus(2))
        Assert.assertEquals(SeriesStatus.TBA, converters.toSeriesStatus(3))
        Assert.assertEquals(SeriesStatus.Unreleased, converters.toSeriesStatus(4))
        Assert.assertEquals(SeriesStatus.Upcoming, converters.toSeriesStatus(5))
    }

    @Test
    fun `fromUserSeriesStatus to id`() {
        Assert.assertEquals(0, converters.fromUserSeriesStatus(UserSeriesStatus.Unknown))
        Assert.assertEquals(1, converters.fromUserSeriesStatus(UserSeriesStatus.Current))
        Assert.assertEquals(2, converters.fromUserSeriesStatus(UserSeriesStatus.Completed))
        Assert.assertEquals(3, converters.fromUserSeriesStatus(UserSeriesStatus.OnHold))
        Assert.assertEquals(4, converters.fromUserSeriesStatus(UserSeriesStatus.Dropped))
        Assert.assertEquals(5, converters.fromUserSeriesStatus(UserSeriesStatus.Planned))
    }

    @Test
    fun `toUserSeriesStatus from id`() {
        Assert.assertEquals(UserSeriesStatus.Unknown, converters.toUserSeriesStatus(0))
        Assert.assertEquals(UserSeriesStatus.Current, converters.toUserSeriesStatus(1))
        Assert.assertEquals(UserSeriesStatus.Completed, converters.toUserSeriesStatus(2))
        Assert.assertEquals(UserSeriesStatus.OnHold, converters.toUserSeriesStatus(3))
        Assert.assertEquals(UserSeriesStatus.Dropped, converters.toUserSeriesStatus(4))
        Assert.assertEquals(UserSeriesStatus.Planned, converters.toUserSeriesStatus(5))
    }
}
