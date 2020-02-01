package com.chesire.nekome.app.series

import android.content.SharedPreferences
import com.chesire.nekome.core.flags.SortOption
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
                    SharedPref.SORT_PREFERENCE,
                    SortOption.Default.index
                )
            } returns SortOption.Title.index
        }

        val classUnderTest = SharedPref(mockPreferences)

        assertEquals(SortOption.Title, classUnderTest.sortPreference)
    }

    @Test
    fun `sortPreference can set in SortOption`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every {
                putInt(
                    SharedPref.SORT_PREFERENCE,
                    SortOption.EndDate.index
                )
            } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SharedPref(mockPreferences)
        classUnderTest.sortPreference = SortOption.EndDate

        verify {
            mockEditor.putInt(
                SharedPref.SORT_PREFERENCE,
                SortOption.EndDate.index
            )
        }
    }

    @Test
    fun `filterPreference returns expected Map`() {
        val expectedJson = """{"0":false,"1":true,"2":false,"3":false,"4":false}"""
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockPreferences = mockk<SharedPreferences> {
            every {
                getString(
                    SharedPref.FILTER_PREFERENCE,
                    defaultFilter
                )
            } returns expectedJson
        }

        val classUnderTest = SharedPref(mockPreferences)

        assertEquals(expectedMap, classUnderTest.filterPreference)
    }

    @Test
    fun `filterPreference can set in Map`() {
        val expectedJson = """{"0":false,"1":true,"2":false,"3":false,"4":false}"""
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockEditor = mockk<SharedPreferences.Editor> {
            every {
                putString(
                    SharedPref.FILTER_PREFERENCE,
                    expectedJson
                )
            } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SharedPref(mockPreferences)
        classUnderTest.filterPreference = expectedMap

        verify {
            mockEditor.putString(
                SharedPref.FILTER_PREFERENCE,
                expectedJson
            )
        }
    }

    @Test
    fun `subscribeToChanges sets up change listener`() {
        val listener = mockk<SharedPreferences.OnSharedPreferenceChangeListener>()
        val mockPreferences = mockk<SharedPreferences> {
            every { registerOnSharedPreferenceChangeListener(any()) } just Runs
        }

        val classUnderTest = SharedPref(mockPreferences)
        classUnderTest.subscribeToChanges(listener)

        verify { mockPreferences.registerOnSharedPreferenceChangeListener(listener) }
    }
}
