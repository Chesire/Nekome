package com.chesire.nekome.injection

import com.chesire.nekome.binders.UserProviderBinder
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.datasource.series.SeriesMapper
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.datasource.series.UserProvider
import com.chesire.nekome.datasource.series.remote.SeriesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dagger [Module] for the [com.chesire.nekome.series] package.
 */
@Module
@InstallIn(SingletonComponent::class)
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
    fun provideSeriesRepository(
        dao: SeriesDao,
        api: SeriesApi,
        user: UserProvider,
        map: SeriesMapper
    ) = SeriesRepository(dao, api, user, map)
}
