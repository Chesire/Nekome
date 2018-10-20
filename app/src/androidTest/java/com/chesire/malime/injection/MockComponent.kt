package com.chesire.malime.injection

import android.app.Application
import com.chesire.malime.MockApplication
import com.chesire.malime.injection.androidmodules.MockActivityModule
import com.chesire.malime.injection.androidmodules.MockServiceModule
import com.chesire.malime.injection.androidmodules.ViewModelModule
import com.chesire.malime.injection.modules.MockAppModule
import com.chesire.malime.injection.modules.MockDatabaseModule
import com.chesire.malime.injection.modules.MockServerModule
import com.chesire.malime.injection.modules.UiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        (AndroidSupportInjectionModule::class),
        (MockActivityModule::class),
        (MockAppModule::class),
        (MockDatabaseModule::class),
        (MockServerModule::class),
        (MockServiceModule::class),
        (UiModule::class),
        (ViewModelModule::class)
    ]
)
interface MockComponent : AndroidInjector<MockApplication> {
    @Component.Builder
    interface Builder {
        fun mockAppModule(mockAppModule: MockAppModule): Builder
        fun mockServerModule(mockServerModule: MockServerModule): Builder

        @BindsInstance
        fun create(application: Application): Builder

        fun build(): MockComponent
    }
}