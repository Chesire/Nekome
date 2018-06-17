package com.chesire.malime.kitsu.models.request

import com.google.gson.annotations.SerializedName

data class RefreshAuthRequest(
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("grant_type")
    val grantType: String = "refresh_token"
)