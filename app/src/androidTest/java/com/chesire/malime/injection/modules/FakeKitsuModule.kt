package com.chesire.malime.injection.modules

import com.chesire.malime.server.api.AuthApi
import com.chesire.malime.server.api.LibraryApi
import com.chesire.malime.server.api.SearchApi
import com.chesire.malime.server.api.UserApi
import com.chesire.malime.harness.FakeAuthApi
import com.chesire.malime.harness.FakeLibraryApi
import com.chesire.malime.harness.FakeSearchApi
import com.chesire.malime.harness.FakeUserApi
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
    abstract fun providesFakeUserApi(fakeUserApi: FakeUserApi): UserApi

    @Binds
    abstract fun providesFakeSearchApi(fakeSearchApi: FakeSearchApi): SearchApi
}
