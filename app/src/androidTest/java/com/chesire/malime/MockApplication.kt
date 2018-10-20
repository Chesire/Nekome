package com.chesire.malime

import android.app.Application
import com.chesire.malime.injection.DaggerMockComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Provides an override of [Application] that sets up the test parameters.
 *
 * Instead of overriding the [MalimeApplication], this should be explicit in what its doing.
 */
class MockApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMockComponent
            .builder()
            .create(this)
            .build()
    }
}