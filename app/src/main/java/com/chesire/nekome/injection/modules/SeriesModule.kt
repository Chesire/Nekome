package com.chesire.nekome.injection.modules

import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.series.SeriesRepository
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
     * Provides the [SeriesRepository] to the dependency graph.
     */
    @Provides
    fun provideSeriesRepository(dao: SeriesDao, api: LibraryApi, user: UserRepository) =
        SeriesRepository(dao, api, user)
}
