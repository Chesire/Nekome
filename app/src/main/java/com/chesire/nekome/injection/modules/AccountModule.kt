package com.chesire.nekome.injection.modules

import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.server.api.UserApi
import dagger.Module
import dagger.Provides

/**
 * Dagger [Module] for the [com.chesire.nekome.account] package.
 */
@Suppress("unused")
@Module
object AccountModule {
    /**
     * Provides the [UserRepository] to the dependency graph.
     */
    @Provides
    fun provideUserRepository(dao: UserDao, api: UserApi) = UserRepository(dao, api)
}
