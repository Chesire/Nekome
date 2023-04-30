package com.chesire.nekome.injection

import com.chesire.nekome.datasource.auth.remote.AuthApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthModule::class]
)
class MockAuthModule {

    @Provides
    @Reusable
    fun provideAuthApi(): AuthApi = mockk<AuthApi>()
}
