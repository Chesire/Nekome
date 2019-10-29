package com.chesire.malime.injection.modules

import com.chesire.malime.account.UserRepository
import com.chesire.malime.database.dao.SeriesDao
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.api.LibraryApi
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
object SeriesModule {
    @Provides
    fun provideUserRepository(dao: SeriesDao, api: LibraryApi, user: UserRepository) =
        SeriesRepository(dao, api, user)
}
