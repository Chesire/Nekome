package com.chesire.malime.injection

import android.app.Application
import com.chesire.malime.MalimeApplication
import com.chesire.malime.injection.androidmodules.ActivityModule
import com.chesire.malime.injection.androidmodules.ServiceModule
import com.chesire.malime.injection.modules.AppModule
import com.chesire.malime.injection.modules.DatabaseModule
import com.chesire.malime.injection.modules.ServerModule
import com.chesire.malime.injection.modules.UiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        (ActivityModule::class),
        (AndroidInjectionModule::class),
        (AppModule::class),
        (DatabaseModule::class),
        (ServerModule::class),
        (ServiceModule::class),
        (UiModule::class)
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(malimeApp: MalimeApplication)
}