package com.chesire.malime.util.extension

import android.content.Context
import com.chesire.malime.R
import com.chesire.malime.core.flags.Subtype
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Test

private const val ONA = "ONA"
private const val OVA = "OVA"
private const val TV = "TV"
private const val MOVIE = "Movie"
private const val MUSIC = "Music"
private const val SPECIAL = "Special"
private const val DOUJIN = "Doujin"
private const val MANGA = "Manga"
private const val MANHUA = "Manhua"
private const val MANHWA = "Manhwa"
private const val NOVEL = "Novel"
private const val OEL = "OEL"
private const val ONESHOT = "Oneshot"
private const val UNKNOWN = "Unknown"

class SubtypeExtensionTests {
    private val mockContext: Context = mock {
        on { getString(R.string.series_subtype_ona) }.thenReturn(ONA)
        on { getString(R.string.series_subtype_ova) }.thenReturn(OVA)
        on { getString(R.string.series_subtype_tv) }.thenReturn(TV)
        on { getString(R.string.series_subtype_movie) }.thenReturn(MOVIE)
        on { getString(R.string.series_subtype_music) }.thenReturn(MUSIC)
        on { getString(R.string.series_subtype_special) }.thenReturn(SPECIAL)
        on { getString(R.string.series_subtype_doujin) }.thenReturn(DOUJIN)
        on { getString(R.string.series_subtype_manga) }.thenReturn(MANGA)
        on { getString(R.string.series_subtype_manhua) }.thenReturn(MANHUA)
        on { getString(R.string.series_subtype_manhwa) }.thenReturn(MANHWA)
        on { getString(R.string.series_subtype_novel) }.thenReturn(NOVEL)
        on { getString(R.string.series_subtype_oel) }.thenReturn(OEL)
        on { getString(R.string.series_subtype_oneshot) }.thenReturn(ONESHOT)
        on { getString(R.string.series_subtype_unknown) }.thenReturn(UNKNOWN)
    }

    @Test
    fun `when Subtype is 'ONA' expect correct string`() {
        Assert.assertEquals(ONA, Subtype.ONA.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'OVA' expect correct string`() {
        Assert.assertEquals(OVA, Subtype.OVA.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'TV' expect correct string`() {
        Assert.assertEquals(TV, Subtype.TV.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Movie' expect correct string`() {
        Assert.assertEquals(MOVIE, Subtype.Movie.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Music' expect correct string`() {
        Assert.assertEquals(MUSIC, Subtype.Music.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Special' expect correct string`() {
        Assert.assertEquals(SPECIAL, Subtype.Special.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Doujin' expect correct string`() {
        Assert.assertEquals(DOUJIN, Subtype.Doujin.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Manga' expect correct string`() {
        Assert.assertEquals(MANGA, Subtype.Manga.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Manhua' expect correct string`() {
        Assert.assertEquals(MANHUA, Subtype.Manhua.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Manhwa' expect correct string`() {
        Assert.assertEquals(MANHWA, Subtype.Manhwa.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Novel' expect correct string`() {
        Assert.assertEquals(NOVEL, Subtype.Novel.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'OEL' expect correct string`() {
        Assert.assertEquals(OEL, Subtype.OEL.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Oneshot' expect correct string`() {
        Assert.assertEquals(ONESHOT, Subtype.Oneshot.getString(mockContext))
    }

    @Test
    fun `when Subtype is 'Unknown' expect correct string`() {
        Assert.assertEquals(UNKNOWN, Subtype.Unknown.getString(mockContext))
    }
}
