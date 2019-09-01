package com.chesire.malime.core

import android.content.SharedPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

private const val defaultFilter = """{"0":true,"1":false,"2":false,"3":false,"4":false}"""

class SharedPrefTests {

    @Test
    fun `sortPreference returns expected SortOption`() {
        val mockPreferences = mockk<SharedPreferences> {
            every {
                getInt(
                    com.chesire.malime.core.SharedPref.SORT_PREFERENCE,
                    com.chesire.malime.core.flags.SortOption.Default.index
                )
            } returns com.chesire.malime.core.flags.SortOption.Title.index
        }

        val classUnderTest = com.chesire.malime.core.SharedPref(mockPreferences)

        assertEquals(com.chesire.malime.core.flags.SortOption.Title, classUnderTest.sortPreference)
    }

    @Test
    fun `sortPreference can set in SortOption`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putInt(com.chesire.malime.core.SharedPref.SORT_PREFERENCE, com.chesire.malime.core.flags.SortOption.EndDate.index) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = com.chesire.malime.core.SharedPref(mockPreferences)
        classUnderTest.sortPreference = com.chesire.malime.core.flags.SortOption.EndDate

        verify { mockEditor.putInt(com.chesire.malime.core.SharedPref.SORT_PREFERENCE, com.chesire.malime.core.flags.SortOption.EndDate.index) }
    }

    @Test
    fun `filterPreference returns expected Map`() {
        val expectedJson = """{"0":false,"1":true,"2":false,"3":false,"4":false}"""
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockPreferences = mockk<SharedPreferences> {
            every { getString(com.chesire.malime.core.SharedPref.FILTER_PREFERENCE,
                defaultFilter
            ) } returns expectedJson
        }

        val classUnderTest = com.chesire.malime.core.SharedPref(mockPreferences)

        assertEquals(expectedMap, classUnderTest.filterPreference)
    }

    @Test
    fun `filterPreference can set in Map`() {
        val expectedJson = """{"0":false,"1":true,"2":false,"3":false,"4":false}"""
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString(com.chesire.malime.core.SharedPref.FILTER_PREFERENCE, expectedJson) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = com.chesire.malime.core.SharedPref(mockPreferences)
        classUnderTest.filterPreference = expectedMap

        verify { mockEditor.putString(com.chesire.malime.core.SharedPref.FILTER_PREFERENCE, expectedJson) }
    }

    @Test
    fun `subscribeToChanges sets up change listener`() {
        val listener = mockk<SharedPreferences.OnSharedPreferenceChangeListener>()
        val mockPreferences = mockk<SharedPreferences> {
            every { registerOnSharedPreferenceChangeListener(any()) } just Runs
        }

        val classUnderTest = com.chesire.malime.core.SharedPref(mockPreferences)
        classUnderTest.subscribeToChanges(listener)

        verify { mockPreferences.registerOnSharedPreferenceChangeListener(listener) }
    }

    @Test
    fun `unsubscribeFromChanges removes change listener`() {
        val listener = mockk<SharedPreferences.OnSharedPreferenceChangeListener>()
        val mockPreferences = mockk<SharedPreferences> {
            every { unregisterOnSharedPreferenceChangeListener(any()) } just Runs
        }

        val classUnderTest = com.chesire.malime.core.SharedPref(mockPreferences)
        classUnderTest.unsubscribeFromChanges(listener)

        verify { mockPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }
}
