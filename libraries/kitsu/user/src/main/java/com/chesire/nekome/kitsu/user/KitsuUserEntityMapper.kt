package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.flags.RatingSystem
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.user.KitsuUserEntity
import com.chesire.nekome.user.api.UserEntity
import com.chesire.nekome.user.api.UserEntityMapper
import javax.inject.Inject

class KitsuUserEntityMapper @Inject constructor() : UserEntityMapper<KitsuUserEntity> {

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
