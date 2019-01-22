package com.chesire.malime.kitsu.api

import com.chesire.malime.core.api.AuthApi

class KitsuAuth(
    private val authService: KitsuAuthService
) : AuthApi {
    override fun login(username: String, password: String) {

    }

    override fun refreshToken() {

    }
}
