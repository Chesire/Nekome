package com.chesire.malime.injection.components

import android.content.Context
import com.chesire.malime.MalimeApplication
import com.chesire.malime.injection.androidmodules.ActivityModule
import com.chesire.malime.injection.androidmodules.FragmentModule
import com.chesire.malime.injection.androidmodules.ViewModelModule
import com.chesire.malime.injection.modules.AppModule
import com.chesire.malime.injection.modules.CoroutineModule
import com.chesire.malime.injection.modules.DatabaseModule
import com.chesire.malime.injection.modules.KitsuModule
import com.chesire.malime.injection.modules.ServerModule
import com.chesire.malime.injection.modules.WorkerModule
import com.chesire.malime.services.RefreshSeriesWorker
import com.chesire.malime.services.RefreshUserWorker
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        CoroutineModule::class,
        DatabaseModule::class,
        FragmentModule::class,
        KitsuModule::class,
        ServerModule::class,
        ViewModelModule::class,
        WorkerModule::class
    ]
)
interface AppComponent : AndroidInjector<MalimeApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): AppComponent
    }

    fun inject(worker: RefreshSeriesWorker)
    fun inject(worker: RefreshUserWorker)
}
