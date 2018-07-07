package com.chesire.malime.core.models

data class AuthModel(
    var authToken: String,
    var refreshToken: String,
    var expireAt: Long,
    var provider: String
)