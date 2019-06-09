package com.chesire.malime

import android.content.Context
import android.content.SharedPreferences
import com.chesire.malime.flow.series.SortOption
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val defaultFilter = """{"0":true,"1":false,"2":false,"3":false,"4":false}"""
private const val analyticsKey = "preference.analytics.enabled"

class SharedPrefTests {

    @Test
    fun `sortPreference returns expected SortOption`() {
        val mockContext = createMockContext()
        val mockPreferences = mockk<SharedPreferences> {
            every {
                getInt(
                    SharedPref.SORT_PREFERENCE,
                    SortOption.Default.index
                )
            } returns SortOption.Title.index
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)

        assertEquals(SortOption.Title, classUnderTest.sortPreference)
    }

    @Test
    fun `sortPreference can set in SortOption`() {
        val mockContext = createMockContext()
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putInt(SharedPref.SORT_PREFERENCE, SortOption.EndDate.index) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)
        classUnderTest.sortPreference = SortOption.EndDate

        verify { mockEditor.putInt(SharedPref.SORT_PREFERENCE, SortOption.EndDate.index) }
    }

    @Test
    fun `filterPreference returns expected Map`() {
        val mockContext = createMockContext()
        val expectedJson = """{"0":false,"1":true,"2":false,"3":false,"4":false}"""
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockPreferences = mockk<SharedPreferences> {
            every { getString(SharedPref.FILTER_PREFERENCE, defaultFilter) } returns expectedJson
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)

        assertEquals(expectedMap, classUnderTest.filterPreference)
    }

    @Test
    fun `filterPreference can set in Map`() {
        val mockContext = createMockContext()
        val expectedJson = """{"0":false,"1":true,"2":false,"3":false,"4":false}"""
        val expectedMap = mapOf(0 to false, 1 to true, 2 to false, 3 to false, 4 to false)
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString(SharedPref.FILTER_PREFERENCE, expectedJson) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)
        classUnderTest.filterPreference = expectedMap

        verify { mockEditor.putString(SharedPref.FILTER_PREFERENCE, expectedJson) }
    }

    @Test
    fun `isAnalyticsEnabled returns expected value`() {
        val mockContext = createMockContext()
        val mockPreferences = mockk<SharedPreferences> {
            every { getBoolean(analyticsKey, false) } returns true
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)

        assertTrue(classUnderTest.isAnalyticsEnabled)
    }

    @Test
    fun `isAnalyticsEnabled can set in Boolean`() {
        val mockContext = createMockContext()
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putBoolean(analyticsKey, true) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)
        classUnderTest.isAnalyticsEnabled = true

        verify { mockEditor.putBoolean(analyticsKey, true) }
    }

    @Test
    fun `isAnalyticsComplete returns expected value`() {
        val mockContext = createMockContext()
        val mockPreferences = mockk<SharedPreferences> {
            every { getBoolean(SharedPref.ANALYTICS_COMPLETE_PREFERENCE, false) } returns true
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)

        assertTrue(classUnderTest.isAnalyticsComplete)
    }

    @Test
    fun `isAnalyticsComplete can set in Boolean`() {
        val mockContext = createMockContext()
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putBoolean(SharedPref.ANALYTICS_COMPLETE_PREFERENCE, true) } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)
        classUnderTest.isAnalyticsComplete = true

        verify { mockEditor.putBoolean(SharedPref.ANALYTICS_COMPLETE_PREFERENCE, true) }
    }

    @Test
    fun `subscribeToChanges sets up change listener`() {
        val mockContext = createMockContext()
        val listener = mockk<SharedPreferences.OnSharedPreferenceChangeListener>()
        val mockPreferences = mockk<SharedPreferences> {
            every { registerOnSharedPreferenceChangeListener(any()) } just Runs
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)
        classUnderTest.subscribeToChanges(listener)

        verify { mockPreferences.registerOnSharedPreferenceChangeListener(listener) }
    }

    @Test
    fun `unsubscribeFromChanges removes change listener`() {
        val mockContext = createMockContext()
        val listener = mockk<SharedPreferences.OnSharedPreferenceChangeListener>()
        val mockPreferences = mockk<SharedPreferences> {
            every { unregisterOnSharedPreferenceChangeListener(any()) } just Runs
        }

        val classUnderTest = SharedPref(mockContext, mockPreferences)
        classUnderTest.unsubscribeFromChanges(listener)

        verify { mockPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    private fun createMockContext() = mockk<Context> {
        every { getString(R.string.preference_analytics_enabled) } returns analyticsKey
    }
}
