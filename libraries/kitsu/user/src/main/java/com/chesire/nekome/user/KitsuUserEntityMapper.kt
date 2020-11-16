package com.chesire.nekome.user

import com.chesire.nekome.core.flags.RatingSystem
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.user.api.UserEntity
import com.chesire.nekome.user.api.UserEntityMapper

class KitsuUserEntityMapper : UserEntityMapper<KitsuUserEntity> {

    override fun mapFromUserEntity(entity: UserEntity): KitsuUserEntity {
        return KitsuUserEntity(
            entity.userId,
            KitsuUserEntity.EntityAttributes(
                entity.name,
                "",
                RatingSystem.Unknown,
                entity.avatar,
                entity.coverImage
            )
        )
    }

    override fun mapToUserEntity(item: KitsuUserEntity): UserEntity {
        return UserEntity(
            item.id,
            item.attributes.name,
            item.attributes.avatar ?: ImageModel.empty,
            item.attributes.coverImage ?: ImageModel.empty,
            Service.Kitsu
        )
    }
}
