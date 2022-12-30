package com.chesire.nekome.app.search

import android.content.SharedPreferences
import com.chesire.nekome.app.search.host.data.HostPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchPreferencesTests {
    @Test
    fun `lastSearchType returns expected value`() {
        val expected = 9001
        val mockPreferences = mockk<SharedPreferences> {
            every { getInt("preference.last_search_type", -1) } returns expected
        }
        val testObject = HostPreferences(mockPreferences)

        assertEquals(expected, testObject.lastSearchType)
    }

    @Test
    fun `lastSearchType can set in value`() {
        val expected = 9001
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { apply() } just Runs
            every { putInt("preference.last_search_type", expected) } returns this
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }
        val testObject = HostPreferences(mockPreferences)

        testObject.lastSearchType = expected

        verify { mockEditor.putInt("preference.last_search_type", expected) }
    }
}
