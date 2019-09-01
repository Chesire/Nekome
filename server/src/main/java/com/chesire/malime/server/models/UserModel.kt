package com.chesire.malime.server.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chesire.malime.server.flags.Service

@Entity
data class UserModel(
    val userId: Int,
    val name: String,
    val avatar: ImageModel,
    val coverImage: ImageModel,
    @PrimaryKey
    val service: Service
)
