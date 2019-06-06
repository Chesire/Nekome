package com.chesire.malime.flow.oob

import com.chesire.malime.SharedPref
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class AnalyticsViewModelTests {
    @Test
    fun `saveAnalyticsChoice sets SharedPref isAnalyticsEnabled flag`() {
        val mockPref = mockk<SharedPref> {
            every { isAnalyticsEnabled = true } just Runs
            every { isAnalyticsComplete = true } just Runs
        }

        AnalyticsViewModel(mockPref).run {
            saveAnalyticsChoice(true)
        }

        verify { mockPref.isAnalyticsEnabled = true }
    }

    @Test
    fun `saveAnalyticsChoice sets SharedPref isAnalyticsComplete flag`() {
        val mockPref = mockk<SharedPref> {
            every { isAnalyticsEnabled = true } just Runs
            every { isAnalyticsComplete = true } just Runs
        }

        AnalyticsViewModel(mockPref).run {
            saveAnalyticsChoice(true)
        }

        verify { mockPref.isAnalyticsComplete = true }
    }
}
