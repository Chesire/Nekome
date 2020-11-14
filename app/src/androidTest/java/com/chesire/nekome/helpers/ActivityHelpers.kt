package com.chesire.nekome.helpers

import androidx.test.core.app.ActivityScenario
import com.chesire.nekome.Activity

/**
 * Launches the [Activity] using the [ActivityScenario].
 */
fun launchActivity() = ActivityScenario.launch(Activity::class.java)
