package com.chesire.nekome.injection.modules

import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.series.SeriesRepository
import com.chesire.nekome.server.api.LibraryApi
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
object SeriesModule {
    @Provides
    fun provideUserRepository(dao: SeriesDao, api: LibraryApi, user: UserRepository) =
        SeriesRepository(dao, api, user)
}
