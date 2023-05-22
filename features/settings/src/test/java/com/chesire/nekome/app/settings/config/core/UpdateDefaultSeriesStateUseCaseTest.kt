package com.chesire.nekome.app.settings.config.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.ApplicationPreferences
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateDefaultSeriesStateUseCaseTest {

    private val pref = mockk<ApplicationPreferences>()
    private lateinit var updateDefaultSeriesState: UpdateDefaultSeriesStateUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateDefaultSeriesState = UpdateDefaultSeriesStateUseCase(pref)
    }

    @Test
    fun `When invoking, update pref with new value`() = runTest {
        coEvery { pref.updateDefaultSeriesState(any()) } just runs

        updateDefaultSeriesState(UserSeriesStatus.OnHold)

        coEvery { pref.updateDefaultSeriesState(UserSeriesStatus.OnHold) }
    }
}
