package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.models.UserModel
import com.chesire.malime.kitsu.api.user.GetUserDetailsResponse
import com.squareup.moshi.FromJson

@Suppress("unused")
class UserModelAdapter {
    @FromJson
    fun userModelFromUserDetails(userDetailsResponse: GetUserDetailsResponse): UserModel {
        val details = userDetailsResponse.data.first()
        return UserModel(
            details.id,
            details.attributes.name,
            details.attributes.avatar,
            details.attributes.coverImage
        )
    }
}
