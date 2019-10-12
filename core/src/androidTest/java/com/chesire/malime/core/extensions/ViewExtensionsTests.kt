package com.chesire.malime.core.extensions

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewExtensionsTests {
    private val context = InstrumentationRegistry.getInstrumentation().context

    @Test
    fun visibleIfReturnsViewVISIBLEIfCallbackIsTrue() {
        val view = View(context)
        view.visibleIf { true }
        assertEquals(View.VISIBLE, view.visibility)
    }

    @Test
    fun visibleIfReturnsViewGONEIfCallbackIsFalse() {
        val view = View(context)
        view.visibleIf { false }
        assertEquals(View.GONE, view.visibility)
    }

    @Test
    fun visibleIfReturnsViewINVISIBLEIfCallbackIsFalseWithOverride() {
        val view = View(context)
        view.visibleIf(invisible = true) { false }
        assertEquals(View.INVISIBLE, view.visibility)
    }
}
