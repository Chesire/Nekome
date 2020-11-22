package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.user.api.UserEntity
import javax.inject.Inject

/**
 * Provides ability to map instances of [KitsuUserEntity] into [UserEntity].
 */
class KitsuUserEntityMapper @Inject constructor() : EntityMapper<KitsuUserEntity, UserEntity> {

    override fun from(input: KitsuUserEntity) = UserEntity(
        input.id,
        input.attributes.name,
        input.attributes.avatar ?: ImageModel.empty,
        input.attributes.coverImage ?: ImageModel.empty,
        Service.Kitsu
    )
}
