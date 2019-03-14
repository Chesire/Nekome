package com.chesire.malime

import com.chesire.malime.injection.components.DaggerTestComponent
import com.chesire.malime.injection.components.TestComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.mockk.spyk

/**
 * Overridden application object that provides a dagger component.
 */
class TestApplication : DaggerApplication() {
    lateinit var component: TestComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestComponent
            .builder()
            .applicationContext(this)
            .authApi(spyk())
            .libraryApi(spyk())
            .userApi(spyk())
            .build()
            .also {
                component = it
            }
    }
}
