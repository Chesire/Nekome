package com.chesire.nekome.account

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.user.api.UserDomain
import javax.inject.Inject

/**
 * Provides ability to map instances of [UserDomain] into [UserModel].
 */
class UserDomainMapper @Inject constructor() : EntityMapper<UserDomain, UserModel> {

    override fun from(input: UserDomain) =
        UserModel(
            input.userId,
            input.name,
            input.avatar,
            input.coverImage,
            input.service
        )
}
