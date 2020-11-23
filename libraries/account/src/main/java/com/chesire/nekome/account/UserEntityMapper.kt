package com.chesire.nekome.account

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.user.api.UserEntity
import javax.inject.Inject

/**
 * Provides ability to map instances of [UserEntity] into [UserModel].
 */
class UserEntityMapper @Inject constructor() : EntityMapper<UserEntity, UserModel> {

    override fun from(input: UserEntity) =
        UserModel(
            input.userId,
            input.name,
            input.avatar,
            input.coverImage,
            input.service
        )
}
