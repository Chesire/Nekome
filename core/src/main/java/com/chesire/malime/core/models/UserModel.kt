package com.chesire.malime.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chesire.malime.core.flags.Service

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
