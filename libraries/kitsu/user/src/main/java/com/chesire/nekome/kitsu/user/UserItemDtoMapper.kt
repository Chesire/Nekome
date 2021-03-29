package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.user.UserDomain
import com.chesire.nekome.kitsu.user.dto.UserItemDto
import javax.inject.Inject

/**
 * Provides ability to map instances of [UserItemDto] into [UserDomain].
 */
class UserItemDtoMapper @Inject constructor() {

    /**
     * Converts an instance of [UserItemDto] into a [UserDomain].
     */
    fun toUserDomain(input: UserItemDto) =
        UserDomain(
            input.id,
            input.attributes.name,
            input.attributes.avatar ?: ImageModel.empty,
            input.attributes.coverImage ?: ImageModel.empty,
            Service.Kitsu
        )
}
