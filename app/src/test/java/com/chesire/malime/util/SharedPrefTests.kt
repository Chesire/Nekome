package com.chesire.malime.util

import android.content.Context
import android.content.SharedPreferences
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.preferences.SortOption
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.RETURNS_SELF
import org.mockito.Mockito.`when`

private const val SHARED_PREF_FILE_NAME = "sharedpreffilename"
private const val UPDATE_SCHEDULER_ENABLED = "updateschedulerenabled"
private const val REFRESH_SCHEDULER_ENABLED = "refreshschedulerenabled"
private const val FORCE_BLOCK_SERVICES = "forceblockservices"
private const val PRIMARY_SERVICE = "primaryservice"
private const val ANIME_FILTER_LENGTH = "animefilterlength"
private const val FILTER = "filter"
private const val SORT_OPTION = "sortoption"

class SharedPrefTests {
    private val mockContext = mock<Context> {
        on { getString(R.string.key_shared_pref_file_name) }.thenReturn(SHARED_PREF_FILE_NAME)
        on { getString(R.string.key_update_scheduler_enabled) }.thenReturn(UPDATE_SCHEDULER_ENABLED)
        on {
            getString(R.string.key_refresh_scheduler_enabled)
        }.thenReturn(REFRESH_SCHEDULER_ENABLED)
        on { getString(R.string.key_force_block_services) }.thenReturn(FORCE_BLOCK_SERVICES)
        on { getString(R.string.key_primary_service) }.thenReturn(PRIMARY_SERVICE)
        on { getString(R.string.key_anime_filter_length) }.thenReturn(ANIME_FILTER_LENGTH)
        on { getString(R.string.key_filter) }.thenReturn(FILTER)
        on { getString(R.string.key_sort) }.thenReturn(SORT_OPTION)
    }

    @Test
    fun `primaryService get() returns SupportedService`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
            on {
                getString(PRIMARY_SERVICE, SupportedService.Unknown.name)
            }.thenReturn(SupportedService.Kitsu.name)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        assertEquals(SupportedService.Kitsu, classUnderTest.primaryService)
    }

    @Test
    fun `primaryService set() changes sharedPreferences value`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        classUnderTest.primaryService = SupportedService.MyAnimeList
        verify(mockPrefEditor).putString(PRIMARY_SERVICE, SupportedService.MyAnimeList.name)
    }

    // TODO add tests for the filter
    // TODO refactor the filter method after tests
    fun filterTests() {}


    @Test
    fun `sortOption get() returns SortOption`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
            on {
                getInt(SORT_OPTION, SortOption.Title.id)
            }.thenReturn(SortOption.Default.id)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        assertEquals(SortOption.Default, classUnderTest.sortOption)
    }

    @Test
    fun `sortOption set() changes sharedPreferences value`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        classUnderTest.sortOption = SortOption.StartDate
        verify(mockPrefEditor).putInt(SORT_OPTION, SortOption.StartDate.id)
    }

    @Test
    fun `seriesUpdateSchedulerEnabled get() returns enabled state`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
            on {
                getBoolean(UPDATE_SCHEDULER_ENABLED, false)
            }.thenReturn(true)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        assertTrue(classUnderTest.seriesUpdateSchedulerEnabled)
    }

    @Test
    fun `seriesUpdateSchedulerEnabled set() changes sharedPreferences value`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        classUnderTest.seriesUpdateSchedulerEnabled = false
        verify(mockPrefEditor).putBoolean(UPDATE_SCHEDULER_ENABLED, false)
    }

    @Test
    fun `refreshTokenSchedulerEnabled get() returns enabled state`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
            on {
                getBoolean(REFRESH_SCHEDULER_ENABLED, false)
            }.thenReturn(true)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        assertTrue(classUnderTest.refreshTokenSchedulerEnabled)
    }

    @Test
    fun `refreshTokenSchedulerEnabled set() changes sharedPreferences value`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        classUnderTest.refreshTokenSchedulerEnabled = false
        verify(mockPrefEditor).putBoolean(REFRESH_SCHEDULER_ENABLED, false)
    }

    @Test
    fun `forceBlockServices get() returns if services are blocked`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
            on {
                getBoolean(FORCE_BLOCK_SERVICES, false)
            }.thenReturn(true)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        assertTrue(classUnderTest.forceBlockServices)
    }

    @Test
    fun `forceBlockServices set() changes sharedPreferences value`() {
        val mockPrefEditor = mock<SharedPreferences.Editor>(defaultAnswer = RETURNS_SELF) { }
        val mockPreferences = mock<SharedPreferences> {
            on { edit() }.thenReturn(mockPrefEditor)
        }
        `when`(
            mockContext.getSharedPreferences(
                SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        ).thenReturn(mockPreferences)

        val classUnderTest = SharedPref(mockContext)

        classUnderTest.forceBlockServices = false
        verify(mockPrefEditor).putBoolean(FORCE_BLOCK_SERVICES, false)
    }
}
