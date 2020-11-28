package com.chesire.nekome.helpers.creation

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.user.api.UserEntity

/**
 * Creates a new [UserEntity].
 */
fun createUserEntity() =
    UserEntity(
        1,
        "name",
        ImageModel.empty,
        ImageModel.empty,
        Service.Kitsu
    )
