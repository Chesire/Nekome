package com.chesire.nekome.core.preferences.flags

import org.junit.Assert.assertEquals
import org.junit.Test

class TitleLanguageTests {

    @Test
    fun `forIndex TitleLanguage#Canonical returns expected value`() {
        assertEquals(
            TitleLanguage.Canonical,
            TitleLanguage.forIndex(0)
        )
    }

    @Test
    fun `forIndex TitleLanguage#English returns expected value`() {
        assertEquals(
            TitleLanguage.English,
            TitleLanguage.forIndex(1)
        )
    }

    @Test
    fun `forIndex TitleLanguage#Romaji returns expected value`() {
        assertEquals(
            TitleLanguage.Romaji,
            TitleLanguage.forIndex(2)
        )
    }

    @Test
    fun `forIndex TitleLanguage#Japanese returns expected value`() {
        assertEquals(
            TitleLanguage.Japanese,
            TitleLanguage.forIndex(3)
        )
    }
}
