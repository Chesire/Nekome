package com.chesire.nekome.core

import android.content.Context
import android.content.SharedPreferences
import com.chesire.nekome.core.flags.UserSeriesStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ApplicationSettingsTests {
    private val mockContext = mockk<Context> {
        every { getString(R.string.key_default_series_state) } returns "key_default_series_state"
    }

    @Test
    fun `can get defaultSeriesState`() {
        val mockPreferences = mockk<SharedPreferences> {
            every { getString("key_default_series_state", "0") } returns "2"
        }
        val testObject = ApplicationSettings(mockContext, mockPreferences)

        assertEquals(UserSeriesStatus.OnHold, testObject.defaultSeriesState)
    }
}
