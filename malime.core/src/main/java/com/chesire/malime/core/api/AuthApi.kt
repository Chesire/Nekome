package com.chesire.malime.core.api

import com.chesire.malime.core.models.AuthModel
import io.reactivex.Single

interface AuthApi {
    fun login(username: String, password: String): Single<AuthModel>
    fun getNewAuthToken(refreshToken: String): Single<AuthModel>
    fun getUserId(): Single<Int>
}