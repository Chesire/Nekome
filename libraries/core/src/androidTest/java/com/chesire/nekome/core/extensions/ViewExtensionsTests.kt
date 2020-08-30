package com.chesire.nekome.core.extensions

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewExtensionsTests {
    private val context = InstrumentationRegistry.getInstrumentation().context

    @Ignore("Ignore this for now, as its having some problems being run in a library")
    @Test
    fun visibleIfReturnsViewVISIBLEIfCallbackIsTrue() {
        val view = View(context)
        view.visibleIf { true }
        assertEquals(View.VISIBLE, view.visibility)
    }

    @Ignore("Ignore this for now, as its having some problems being run in a library")
    @Test
    fun visibleIfReturnsViewGONEIfCallbackIsFalse() {
        val view = View(context)
        view.visibleIf { false }
        assertEquals(View.GONE, view.visibility)
    }

    @Ignore("Ignore this for now, as its having some problems being run in a library")
    @Test
    fun visibleIfReturnsViewINVISIBLEIfCallbackIsFalseWithOverride() {
        val view = View(context)
        view.visibleIf(invisible = true) { false }
        assertEquals(View.INVISIBLE, view.visibility)
    }
}
