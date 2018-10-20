package com.chesire.malime

import android.app.Application
import android.content.Context
import com.github.tmurakami.dexopener.DexOpenerAndroidJUnitRunner

/**
 * Provides a test runner that initializes the test application instead of the real application.
 */
@Suppress("unused")
class MockTestRunner : DexOpenerAndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, "com.chesire.malime.MockApplication", context)
    }
}