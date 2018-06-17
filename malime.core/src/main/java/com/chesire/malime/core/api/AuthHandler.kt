package com.chesire.malime.core.api

import com.chesire.malime.core.models.AuthModel

interface AuthHandler {
    fun getAuth(): AuthModel
    fun setAuth(newModel: AuthModel)
}