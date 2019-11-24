package com.chesire.nekome.helpers

import androidx.test.platform.app.InstrumentationRegistry
import com.chesire.nekome.TestApplication
import com.chesire.nekome.injection.components.TestComponent

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
