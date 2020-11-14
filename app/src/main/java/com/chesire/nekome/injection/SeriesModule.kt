package com.chesire.nekome.injection

import com.chesire.nekome.binders.UserProviderBinder
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.library.UserProvider
import com.chesire.nekome.server.api.LibraryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Dagger [Module] for the [com.chesire.nekome.series] package.
 */
@Module
@InstallIn(ApplicationComponent::class)
object SeriesModule {
    /**
     * Provides a [UserProvider] to the series repository.
     */
    @Provides
    fun bindUserProvider(binder: UserProviderBinder): UserProvider = binder

    /**
     * Provides the [SeriesRepository] to the dependency graph.
     */
    @Provides
    fun provideSeriesRepository(dao: SeriesDao, api: LibraryApi, user: UserProvider) =
        SeriesRepository(dao, api, user)
}
