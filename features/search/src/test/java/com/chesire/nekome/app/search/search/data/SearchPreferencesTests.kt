package com.chesire.nekome.app.search.search.data

import android.content.SharedPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchPreferencesTests {

    @Test
    fun `lastSearchGroup returns expected value`() {
        val expected = "expected"
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("preference.last_search_group", "") } returns expected
        }
        val testObject = SearchPreferences(mockPreferences)

        assertEquals(expected, testObject.lastSearchGroup)
    }

    @Test
    fun `lastSearchGroup can set in value`() {
        val expected = "expected"
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { apply() } just Runs
            every { putString("preference.last_search_group", expected) } returns this
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val testObject = SearchPreferences(mockPreferences)

        testObject.lastSearchGroup = expected

        verify { mockEditor.putString("preference.last_search_group", expected) }
    }
}
