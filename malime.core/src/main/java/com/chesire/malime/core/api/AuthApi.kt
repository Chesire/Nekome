package com.chesire.malime.core.api

import com.chesire.malime.core.models.AuthModel
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue

interface AuthApi {
    @CheckReturnValue
    fun login(username: String, password: String): Single<AuthModel>

    @CheckReturnValue
    fun getNewAuthToken(refreshToken: String): Single<AuthModel>

    @CheckReturnValue
    fun getUserId(): Single<Int>
}
