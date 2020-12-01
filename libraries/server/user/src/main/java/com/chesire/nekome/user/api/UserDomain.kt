package com.chesire.nekome.user.api

import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel

/**
 * Domain class for user related information.
 */
data class UserDomain(
    val userId: Int,
    val name: String,
    val avatar: ImageModel,
    val coverImage: ImageModel,
    val service: Service
)
