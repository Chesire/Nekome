package com.chesire.malime.injection.components

import android.content.Context
import com.chesire.malime.TestApplication
import com.chesire.malime.flow.ActivityTests
import com.chesire.malime.flow.login.DetailsTests
import com.chesire.malime.flow.login.SyncingTests
import com.chesire.malime.harness.FakeAuthApi
import com.chesire.malime.harness.FakeLibraryApi
import com.chesire.malime.harness.FakeSearchApi
import com.chesire.malime.harness.FakeUserApi
import com.chesire.malime.injection.androidmodules.ActivityModule
import com.chesire.malime.injection.androidmodules.FragmentModule
import com.chesire.malime.injection.androidmodules.ViewModelModule
import com.chesire.malime.injection.modules.AppModule
import com.chesire.malime.injection.modules.CoroutinesModule
import com.chesire.malime.injection.modules.FakeKitsuModule
import com.chesire.malime.injection.modules.MemoryDatabaseModule
import com.chesire.malime.injection.modules.ServerModule
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
        CoroutinesModule::class,
        MemoryDatabaseModule::class,
        FakeKitsuModule::class,
        FragmentModule::class,
        ServerModule::class,
        ViewModelModule::class
    ]
)
interface TestComponent : AndroidInjector<TestApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        @BindsInstance
        fun authApi(authApi: FakeAuthApi): Builder

        @BindsInstance
        fun libraryApi(libraryApi: FakeLibraryApi): Builder

        @BindsInstance
        fun searchApi(searchApi: FakeSearchApi): Builder

        @BindsInstance
        fun userApi(userApi: FakeUserApi): Builder

        fun build(): TestComponent
    }

    fun inject(overviewActivityTests: ActivityTests)
    fun inject(detailsTests: DetailsTests)
    fun inject(syncingTests: SyncingTests)
}
