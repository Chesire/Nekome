package com.chesire.malime.core

import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Test

class PreferenceProviderTests {
    @Test
    fun `can get preferences from context`() {
        val mockContext: Context = mock {
            on {
                getSharedPreferences("authFile", Context.MODE_PRIVATE)
            }.thenReturn(mock { })
        }

        val preferenceProvider = PreferenceProvider()

        val pref = preferenceProvider.getPreferencesFor(
            mockContext,
            "authFile",
            Context.MODE_PRIVATE
        )
        Assert.assertNotNull(pref)
    }
}
