package com.chesire.nekome.injection.components

import android.content.Context
import com.chesire.nekome.TestApplication
import com.chesire.nekome.flow.ActivityTests
import com.chesire.nekome.flow.login.DetailsTests
import com.chesire.nekome.flow.login.SyncingTests
import com.chesire.nekome.flow.series.FilterTests
import com.chesire.nekome.flow.series.SeriesListTests
import com.chesire.nekome.flow.series.SeriesViewHolderTests
import com.chesire.nekome.flow.series.SortTests
import com.chesire.nekome.flow.settings.SettingsTests
import com.chesire.nekome.harness.FakeAuthApi
import com.chesire.nekome.harness.FakeLibraryApi
import com.chesire.nekome.harness.FakeSearchApi
import com.chesire.nekome.harness.FakeTrendingApi
import com.chesire.nekome.harness.FakeUrlHandler
import com.chesire.nekome.harness.FakeUserApi
import com.chesire.nekome.injection.modules.AccountModule
import com.chesire.nekome.injection.modules.AppModule
import com.chesire.nekome.injection.modules.CoroutineModule
import com.chesire.nekome.injection.modules.FakeKitsuModule
import com.chesire.nekome.injection.modules.FakeUrlModule
import com.chesire.nekome.injection.modules.MemoryDatabaseModule
import com.chesire.nekome.injection.modules.SeriesModule
import com.chesire.nekome.injection.modules.ServerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AccountModule::class,
        AppModule::class,
        CoroutineModule::class,
        FakeKitsuModule::class,
        FakeUrlModule::class,
        MemoryDatabaseModule::class,
        SeriesModule::class,
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
        fun trendingApi(trendingApi: FakeTrendingApi): Builder

        @BindsInstance
        fun userApi(userApi: FakeUserApi): Builder

        @BindsInstance
        fun urlHandler(url: FakeUrlHandler): Builder

        fun build(): TestComponent
    }

    fun inject(target: ActivityTests)
    fun inject(target: DetailsTests)
    fun inject(target: FilterTests)
    fun inject(target: SeriesListTests)
    fun inject(target: SeriesViewHolderTests)
    fun inject(target: SettingsTests)
    fun inject(target: SortTests)
    fun inject(target: SyncingTests)
}
