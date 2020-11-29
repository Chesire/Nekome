package com.chesire.nekome.injection

import com.chesire.nekome.account.UserDomainMapper
import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.user.api.UserDomain
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
        mapper: UserDomainMapper
    ): EntityMapper<UserDomain, UserModel>
}
