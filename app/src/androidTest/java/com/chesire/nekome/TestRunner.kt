package com.chesire.nekome

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Overridden runner that forces the application object to be [com.chesire.nekome.TestApplication].
 */
@Suppress("unused")
class TestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, "com.chesire.malime.TestApplication", context)
}
