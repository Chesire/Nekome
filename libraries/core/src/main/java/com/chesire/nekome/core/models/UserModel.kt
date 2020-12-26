package com.chesire.nekome.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chesire.nekome.core.flags.Service

/**
 * Data for a singular user entity.
 */
@Entity
data class UserModel(
    val userId: Int,
    val name: String,
    val avatar: ImageModel,
    val coverImage: ImageModel,
    @PrimaryKey
    val service: Service
)
