package com.chesire.nekome.app.series

import android.content.Context
import android.content.SharedPreferences
import com.chesire.nekome.core.flags.SortOption
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val RATE_ON_COMPLETION_KEY = "rateOnCompletionKey"

class SeriesPreferencesTests {

    private val defaultFilter =
        """
{"0":true,"1":false,"2":false,"3":false,"4":false}
        """.trimIndent()

    private val mockContext = mockk<Context> {
        every { getString(R.string.key_rate_on_completion) } returns RATE_ON_COMPLETION_KEY
    }

    @Test
    fun `sortPreference returns expected SortOption`() {
        val mockPreferences = mockk<SharedPreferences> {
            every {
                getInt(
                    SeriesPreferences.SORT_PREFERENCE,
                    SortOption.Default.index
                )
            } returns SortOption.Title.index
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)

        assertEquals(SortOption.Title, classUnderTest.sortPreference)
    }

    @Test
    fun `sortPreference can set in SortOption`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every {
                putInt(
                    SeriesPreferences.SORT_PREFERENCE,
                    SortOption.EndDate.index
                )
            } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)
        classUnderTest.sortPreference = SortOption.EndDate

        verify {
            mockEditor.putInt(
                SeriesPreferences.SORT_PREFERENCE,
                SortOption.EndDate.index
            )
        }
    }

    @Test
    fun `filterPreference returns expected Map`() {
        val expectedJson =
            """
{"0":false,"1":true,"2":false,"3":false,"4":false}
            """.trimIndent()
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockPreferences = mockk<SharedPreferences> {
            every {
                getString(
                    SeriesPreferences.FILTER_PREFERENCE,
                    defaultFilter
                )
            } returns expectedJson
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)

        assertEquals(expectedMap, classUnderTest.filterPreference)
    }

    @Test
    fun `filterPreference can set in Map`() {
        val expectedJson =
            """
{"0":false,"1":true,"2":false,"3":false,"4":false}
            """.trimIndent()
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockEditor = mockk<SharedPreferences.Editor> {
            every {
                putString(
                    SeriesPreferences.FILTER_PREFERENCE,
                    expectedJson
                )
            } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)
        classUnderTest.filterPreference = expectedMap

        verify {
            mockEditor.putString(
                SeriesPreferences.FILTER_PREFERENCE,
                expectedJson
            )
        }
    }

    @Test
    fun `rateSeriesOnCompletion returns value as expected`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { getBoolean(RATE_ON_COMPLETION_KEY, false) } returns true
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)
        val actual = classUnderTest.rateSeriesOnCompletion

        assertTrue(actual)
    }

    @Test
    fun `subscribeToChanges sets up change listener`() {
        val listener = mockk<SharedPreferences.OnSharedPreferenceChangeListener>()
        val mockPreferences = mockk<SharedPreferences> {
            every { registerOnSharedPreferenceChangeListener(any()) } just Runs
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)
        classUnderTest.subscribeToChanges(listener)

        verify { mockPreferences.registerOnSharedPreferenceChangeListener(listener) }
    }
}
