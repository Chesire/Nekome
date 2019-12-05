package com.chesire.nekome.helpers

import androidx.test.platform.app.InstrumentationRegistry

/**
 * Gets a string based on what the value of [this] is.
 */
fun Int.getResource() = InstrumentationRegistry.getInstrumentation().targetContext.getString(this)
