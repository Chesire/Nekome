package com.chesire.nekome.helpers.creation

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.user.api.UserDomain

/**
 * Creates a new [UserDomain].
 */
fun createUserDomain() =
    UserDomain(
        userId = 1,
        name = "name",
        avatar = ImageModel.empty,
        coverImage = ImageModel.empty,
        service = Service.Kitsu
    )
