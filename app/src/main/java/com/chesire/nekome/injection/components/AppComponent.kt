package com.chesire.nekome.injection.components

import android.content.Context
import com.chesire.nekome.App
import com.chesire.nekome.injection.androidmodules.ActivityModule
import com.chesire.nekome.injection.androidmodules.FragmentModule
import com.chesire.nekome.injection.androidmodules.ViewModelModule
import com.chesire.nekome.injection.modules.AccountModule
import com.chesire.nekome.injection.modules.AppModule
import com.chesire.nekome.injection.modules.CoroutineModule
import com.chesire.nekome.injection.modules.DatabaseModule
import com.chesire.nekome.injection.modules.KitsuModule
import com.chesire.nekome.injection.modules.SeriesModule
import com.chesire.nekome.injection.modules.ServerModule
import com.chesire.nekome.injection.modules.UrlModule
import com.chesire.nekome.injection.modules.WorkerModule
import com.chesire.nekome.services.RefreshSeriesWorker
import com.chesire.nekome.services.RefreshUserWorker
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * [Component] that is scoped to the entire application.
 */
@Singleton
@Component(
    modules = [
        AccountModule::class,
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        CoroutineModule::class,
        DatabaseModule::class,
        FragmentModule::class,
        KitsuModule::class,
        SeriesModule::class,
        ServerModule::class,
        ViewModelModule::class,
        UrlModule::class,
        WorkerModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    /**
     * Builder for the [AppComponent].
     */
    @Component.Builder
    interface Builder {
        /**
         * Bind the [Context] of the application to the Dagger graph.
         */
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        /**
         * Builds the [AppComponent].
         */
        fun build(): AppComponent
    }

    /**
     * Provides Dagger injection into the [RefreshSeriesWorker].
     */
    fun inject(worker: RefreshSeriesWorker)

    /**
     * Provides Dagger injection into the [RefreshUserWorker].
     */
    fun inject(worker: RefreshUserWorker)
}
