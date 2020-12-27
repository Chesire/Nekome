package com.chesire.nekome.account

import com.chesire.nekome.core.models.ImageModel

/**
 * Domain class for user related information.
 */
data class UserDomain(
    val userId: Int,
    val name: String,
    val avatar: ImageModel,
    val coverImage: ImageModel
)
