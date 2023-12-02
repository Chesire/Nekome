package com.chesire.nekome.app.search.search.core

import com.chesire.nekome.app.search.search.core.model.SearchGroup
import com.chesire.nekome.app.search.search.data.SearchPreferences
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class RememberSearchGroupUseCaseTest {

    private val searchPreferences = mockk<SearchPreferences>()
    private lateinit var rememberSearchGroup: RememberSearchGroupUseCase

    @Before
    fun setup() {
        clearAllMocks()

        rememberSearchGroup = RememberSearchGroupUseCase(searchPreferences)
    }

    @Test
    fun `When invoke, Then search group is saved to preferences`() {
        every { searchPreferences.lastSearchGroup = any() } just runs

        rememberSearchGroup.invoke(SearchGroup.Manga)

        verify { searchPreferences.lastSearchGroup = "Manga" }
    }
}
