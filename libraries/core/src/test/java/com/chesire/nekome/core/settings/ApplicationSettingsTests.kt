package com.chesire.nekome.core.settings

import android.content.Context
import android.content.SharedPreferences
import com.chesire.nekome.core.R
import com.chesire.nekome.core.flags.HomeScreenOptions
import com.chesire.nekome.dataflags.UserSeriesStatus
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ApplicationSettingsTests {
    private val mockContext = mockk<Context> {
        every { getString(R.string.key_default_home) } returns "key_default_home"
        every { getString(R.string.key_default_series_state) } returns "key_default_series_state"
        every { getString(R.string.key_theme) } returns "key_theme"
    }

    @Test
    fun `can get defaultHomeScreen`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("key_default_home", "0") } returns "1"
        }
        val testObject = ApplicationSettings(
            mockContext,
            mockPreferences
        )

        assertEquals(HomeScreenOptions.Manga, testObject.defaultHomeScreen)
    }

    @Test
    fun `defaultHomeScreen with Unknown value returns Anime`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString("key_default_home", "0") } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("key_default_home", "0") } returns "-1"
            every { edit() } returns mockEditor
        }
        val testObject = ApplicationSettings(
            mockContext,
            mockPreferences
        )

        val result = testObject.defaultHomeScreen

        assertEquals(HomeScreenOptions.Anime, result)
    }

    @Test
    fun `can get defaultSeriesState`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("key_default_series_state", "0") } returns "2"
        }
        val testObject = ApplicationSettings(
            mockContext,
            mockPreferences
        )

        assertEquals(com.chesire.nekome.dataflags.UserSeriesStatus.OnHold, testObject.defaultSeriesState)
    }

    @Test
    fun `defaultSeriesState with Unknown value resets preference value to Current`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString("key_default_series_state", "0") } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("key_default_series_state", "0") } returns "-1"
            every { edit() } returns mockEditor
        }
        val testObject = ApplicationSettings(
            mockContext,
            mockPreferences
        )

        testObject.defaultSeriesState

        verify { mockEditor.putString("key_default_series_state", "0") }
    }

    @Test
    fun `defaultSeriesState with Unknown value returns Current`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every { putString("key_default_series_state", "0") } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("key_default_series_state", "0") } returns "-1"
            every { edit() } returns mockEditor
        }
        val testObject = ApplicationSettings(
            mockContext,
            mockPreferences
        )

        val result = testObject.defaultSeriesState

        assertEquals(com.chesire.nekome.dataflags.UserSeriesStatus.Current, result)
    }

    @Test
    fun `can get theme`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("key_theme", "-1") } returns "2"
        }
        val testObject = ApplicationSettings(
            mockContext,
            mockPreferences
        )

        assertEquals(Theme.Dark, testObject.theme)
    }
}
