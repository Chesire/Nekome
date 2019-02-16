package com.chesire.malime.kitsu.api.user

import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.UserModel
import com.squareup.moshi.FromJson

@Suppress("unused")
class UserModelAdapter {
    @FromJson
    fun userModelFromUserDetails(response: GetUserDetailsResponse): UserModel {
        val details = response.data.first()
        return UserModel(
            details.id,
            details.attributes.name,
            details.attributes.avatar ?: ImageModel.empty,
            details.attributes.coverImage ?: ImageModel.empty
        )
    }
}
