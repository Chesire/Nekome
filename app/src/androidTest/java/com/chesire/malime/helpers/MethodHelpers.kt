package com.chesire.malime.helpers

import androidx.test.platform.app.InstrumentationRegistry
import com.chesire.malime.TestApplication
import com.chesire.malime.injection.components.TestComponent

/**
 * Gets the test context as [TestApplication].
 */
fun getApplication() = InstrumentationRegistry
    .getInstrumentation()
    .targetContext
    .applicationContext as TestApplication

/**
 * Gets the required component to inject test files.
 */
val injector: TestComponent
    get() = getApplication().component
