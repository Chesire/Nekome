package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.auth.api.AuthEntity
import com.chesire.nekome.auth.api.AuthEntityMapper
import javax.inject.Inject

// TODO #387
class KitsuAuthEntityMapper @Inject constructor() : AuthEntityMapper<KitsuAuthEntity> {
    override fun mapFromAuthEntity(entity: AuthEntity): KitsuAuthEntity {
        return KitsuAuthEntity(
            "",
            0L,
            0L,
            "",
            "",
            ""
        )
    }

    override fun mapToAuthEntity(item: KitsuAuthEntity): AuthEntity {
        return AuthEntity()
    }
}
