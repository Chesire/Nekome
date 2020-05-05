package com.chesire.nekome.injection.modules

import com.chesire.nekome.binders.UserProviderBinder
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.series.SeriesRepository
import com.chesire.nekome.series.UserProvider
import com.chesire.nekome.server.api.LibraryApi
import dagger.Module
import dagger.Provides

/**
 * Dagger [Module] for the [com.chesire.nekome.series] package.
 */
@Suppress("unused")
@Module
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
