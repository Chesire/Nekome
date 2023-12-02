package com.chesire.nekome.app.search.search.core

import com.chesire.nekome.app.search.search.core.model.SearchGroup
import com.chesire.nekome.app.search.search.data.SearchPreferences
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchInitializeUseCaseTest {

    private val searchPreferences = mockk<SearchPreferences>()
    private lateinit var hostInitialize: SearchInitializeUseCase

    @Before
    fun setup() {
        clearAllMocks()

        hostInitialize = SearchInitializeUseCase(searchPreferences)
    }

    @Test
    fun `Given no previous search group, When invoke, Then initialGroup is Anime`() {
        every { searchPreferences.lastSearchGroup } returns ""

        val result = hostInitialize.invoke()

        assertResult(SearchGroup.Anime, result)
    }

    @Test
    fun `Given previous search group of Manga, When invoke, Then initialGroup is Manga`() {
        every { searchPreferences.lastSearchGroup } returns "Manga"

        val result = hostInitialize.invoke()

        assertResult(SearchGroup.Manga, result)
    }

    @Test
    fun `Given previous search group is invalid, When invoke, Then initialGroup is Anime`() {
        every { searchPreferences.lastSearchGroup } returns "invalid"

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
