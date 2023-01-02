package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.app.search.host.data.HostPreferences
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HostInitializeUseCaseTest {

    private val hostPreferences = mockk<HostPreferences>()
    private lateinit var hostInitialize: HostInitializeUseCase

    @Before
    fun setup() {
        clearAllMocks()

        hostInitialize = HostInitializeUseCase(hostPreferences)
    }

    @Test
    fun `Given no previous search group, When invoke, Then initialGroup is Anime`() {
        every { hostPreferences.lastSearchGroup } returns ""

        val result = hostInitialize.invoke()

        assertResult(SearchGroup.Anime, result)
    }

    @Test
    fun `Given previous search group of Manga, When invoke, Then initialGroup is Manga`() {
        every { hostPreferences.lastSearchGroup } returns "Manga"

        val result = hostInitialize.invoke()

        assertResult(SearchGroup.Manga, result)
    }

    @Test
    fun `Given previous search group is invalid, When invoke, Then initialGroup is Anime`() {
        every { hostPreferences.lastSearchGroup } returns "invalid"

        val result = hostInitialize.invoke()

        assertResult(SearchGroup.Anime, result)
    }

    private fun assertResult(expectedSearchGroup: SearchGroup, result: InitializeArgs) {
        assertEquals(
            InitializeArgs(
                initialGroup = expectedSearchGroup
            ),
            result
        )
    }
}
