package com.chesire.nekome.account

import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.user.api.UserDomain
import javax.inject.Inject

/**
 * Provides ability to map instances of [UserDomain] into [UserModel].
 */
class UserDomainMapper @Inject constructor() {

    /**
     * Converts an instance of [UserDomain] into an instance of [UserModel].
     */
    fun toUserModel(input: UserDomain) =
        UserModel(
            input.userId,
            input.name,
            input.avatar,
            input.coverImage,
            input.service
        )
}
