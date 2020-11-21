package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.auth.api.AuthEntity
import com.chesire.nekome.core.EntityMapper
import javax.inject.Inject

// TODO #387
class KitsuAuthEntityMapper @Inject constructor() : EntityMapper<KitsuAuthEntity, AuthEntity> {
    override fun from(input: KitsuAuthEntity) = AuthEntity()
}
