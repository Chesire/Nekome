package com.chesire.nekome.app.series

import android.content.Context
import android.content.SharedPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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
    fun `rateSeriesOnCompletion returns value as expected`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { getBoolean(RATE_ON_COMPLETION_KEY, false) } returns true
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)
        val actual = classUnderTest.rateSeriesOnCompletionPreference

        assertTrue(actual)
    }

    @Test
    fun `rateSeriesOnCompletion can set value`() {
        val mockEditor = mockk<SharedPreferences.Editor> {
            every {
                putBoolean(RATE_ON_COMPLETION_KEY, true)
            } returns this
            every { apply() } just Runs
        }
        val mockPreferences = mockk<SharedPreferences> {
            every { edit() } returns mockEditor
        }

        val classUnderTest = SeriesPreferences(mockPreferences, mockContext)
        classUnderTest.rateSeriesOnCompletionPreference = true

        verify { mockEditor.putBoolean(RATE_ON_COMPLETION_KEY, true) }
    }
}
