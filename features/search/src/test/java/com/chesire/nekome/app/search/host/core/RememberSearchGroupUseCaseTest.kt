package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.app.search.host.data.HostPreferences
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class RememberSearchGroupUseCaseTest {

    private val hostPreferences = mockk<HostPreferences>()
    private lateinit var rememberSearchGroup: RememberSearchGroupUseCase

    @Before
    fun setup() {
        clearAllMocks()

        rememberSearchGroup = RememberSearchGroupUseCase(hostPreferences)
    }

    @Test
    fun `When invoke, Then search group is saved to preferences`() {
        every { hostPreferences.lastSearchGroup = any() } just runs

        rememberSearchGroup.invoke(SearchGroup.Manga)

        verify { hostPreferences.lastSearchGroup = "Manga" }
    }
}
