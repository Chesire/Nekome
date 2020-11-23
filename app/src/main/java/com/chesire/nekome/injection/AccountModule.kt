package com.chesire.nekome.injection

import com.chesire.nekome.account.UserEntityMapper
import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.user.api.UserEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Provides a Hilt module to aid with using the account library.
 */
@Module
@InstallIn(ApplicationComponent::class)
abstract class AccountModule {

    /**
     * Binds [mapper] to an instance of [EntityMapper].
     */
    @Binds
    abstract fun bindEntityMapper(
        mapper: UserEntityMapper
    ): EntityMapper<UserEntity, UserModel>
}
