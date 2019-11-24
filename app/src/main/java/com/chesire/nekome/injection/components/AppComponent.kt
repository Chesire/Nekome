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
import com.chesire.nekome.injection.modules.WorkerModule
import com.chesire.nekome.services.RefreshSeriesWorker
import com.chesire.nekome.services.RefreshUserWorker
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

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
        WorkerModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): AppComponent
    }

    fun inject(worker: RefreshSeriesWorker)
    fun inject(worker: RefreshUserWorker)
}
