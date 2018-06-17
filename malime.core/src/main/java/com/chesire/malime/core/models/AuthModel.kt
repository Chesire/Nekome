package com.chesire.malime.core.models

class AuthModel(
    val authToken: String,
    val refreshToken: String,
    val expireAt: Long
)