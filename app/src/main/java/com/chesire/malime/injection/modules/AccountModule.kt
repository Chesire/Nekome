package com.chesire.malime.injection.modules

import com.chesire.malime.account.UserRepository
import com.chesire.malime.database.dao.UserDao
import com.chesire.malime.server.api.UserApi
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
object AccountModule {
    @Provides
    fun provideUserRepository(dao: UserDao, api: UserApi) = UserRepository(dao, api)
}
