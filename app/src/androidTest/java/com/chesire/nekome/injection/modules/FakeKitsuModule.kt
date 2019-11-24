package com.chesire.nekome.injection.modules

import com.chesire.nekome.harness.FakeAuthApi
import com.chesire.nekome.harness.FakeLibraryApi
import com.chesire.nekome.harness.FakeSearchApi
import com.chesire.nekome.harness.FakeTrendingApi
import com.chesire.nekome.harness.FakeUserApi
import com.chesire.nekome.server.api.AuthApi
import com.chesire.nekome.server.api.LibraryApi
import com.chesire.nekome.server.api.SearchApi
import com.chesire.nekome.server.api.TrendingApi
import com.chesire.nekome.server.api.UserApi
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class FakeKitsuModule {
    @Binds
    abstract fun providesFakeAuthApi(fakeAuthApi: FakeAuthApi): AuthApi

    @Binds
    abstract fun providesFakeLibraryApi(fakeLibraryApi: FakeLibraryApi): LibraryApi

    @Binds
    abstract fun providesFakeSearchApi(fakeSearchApi: FakeSearchApi): SearchApi

    @Binds
    abstract fun providesFakeTrendingApi(fakeTrendingApi: FakeTrendingApi): TrendingApi

    @Binds
    abstract fun providesFakeUserApi(fakeUserApi: FakeUserApi): UserApi
}
