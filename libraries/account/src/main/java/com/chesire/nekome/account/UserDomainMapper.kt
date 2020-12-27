package com.chesire.nekome.account

import com.chesire.nekome.database.entity.UserEntity
import com.chesire.nekome.user.api.UserDomain
import javax.inject.Inject

/**
 * Provides ability to map instances of [UserDomain].
 */
class UserDomainMapper @Inject constructor() {

    /**
     * Converts an instance of [UserDomain] into an instance of [UserEntity].
     */
    fun toUserEntity(input: UserDomain) =
        UserEntity(
            input.userId,
            input.name,
            input.avatar,
            input.coverImage,
            input.service
        )

    /**
     * Converts an instance of [UserEntity] into an instance of [UserDomain].
     */
    fun toUserDomain(input: UserEntity) =
        UserDomain(
            input.userId,
            input.name,
            input.avatar,
            input.coverImage
        )
}
