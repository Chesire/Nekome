package com.chesire.nekome.injection

import com.chesire.nekome.datasource.search.remote.SearchApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SearchModule::class]
)
class MockSearchModule {

    @Provides
    @Reusable
    fun provideApi() = mockk<SearchApi>()
}
