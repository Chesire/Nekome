package com.chesire.nekome

import com.chesire.nekome.injection.components.DaggerTestComponent
import com.chesire.nekome.injection.components.TestComponent
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
            .searchApi(spyk())
            .trendingApi(spyk())
            .userApi(spyk())
            .urlHandler(spyk())
            .build()
            .also {
                component = it
            }
    }
}
