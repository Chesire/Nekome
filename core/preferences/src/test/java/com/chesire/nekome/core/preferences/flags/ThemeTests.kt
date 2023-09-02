package com.chesire.nekome.core.preferences.flags

import android.content.Context
import com.chesire.nekome.resources.StringResource
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeTests {

    @Test
    fun `getValueMap returns expected map`() {
        val mockContext = mockk<Context> {
            every { getString(StringResource.settings_theme_system) } returns "system"
            every { getString(StringResource.settings_theme_dark) } returns "dark"
            every { getString(StringResource.settings_theme_light) } returns "light"
            every { getString(StringResource.settings_theme_dynamic_dark) } returns "dynamicDark"
            every { getString(StringResource.settings_theme_dynamic_light) } returns "dynamicLight"
        }
        val map = Theme.getValueMap(mockContext)

        assertEquals("system", map.getValue(-1))
        assertEquals("dark", map.getValue(2))
        assertEquals("light", map.getValue(1))
        assertEquals("dynamicDark", map.getValue(3))
        assertEquals("dynamicLight", map.getValue(4))
    }

    @Test
    fun `fromValue with Theme#System returns expected value`() {
        assertEquals(
            Theme.System,
            Theme.fromValue("-1")
        )
    }

    @Test
    fun `fromValue with Theme#Dark returns expected value`() {
        assertEquals(
            Theme.Dark,
            Theme.fromValue("2")
        )
    }

    @Test
    fun `fromValue with Theme#Light returns expected value`() {
        assertEquals(
            Theme.Light,
            Theme.fromValue("1")
        )
    }

    @Test
    fun `fromValue with Theme#DynamicDark returns expected value`() {
        assertEquals(
            Theme.DynamicDark,
            Theme.fromValue("3")
        )
    }

    @Test
    fun `fromValue with Theme#DynamicLight returns expected value`() {
        assertEquals(
            Theme.DynamicLight,
            Theme.fromValue("4")
        )
    }

    @Test
    fun `fromValue with unexpected value returns default of System`() {
        assertEquals(
            Theme.System,
            Theme.fromValue("999")
        )
    }

    @Test
    fun `fromValue with non-numerical value returns default of System`() {
        assertEquals(
            Theme.System,
            Theme.fromValue("parse")
        )
    }
}
