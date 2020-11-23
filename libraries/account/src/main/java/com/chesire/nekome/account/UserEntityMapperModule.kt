package com.chesire.nekome.account

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.user.api.UserEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class UserEntityMapperModule {

    @Binds
    abstract fun bindEntityMapper(
        mapper: UserEntityMapper
    ): EntityMapper<UserEntity, UserModel>
}
