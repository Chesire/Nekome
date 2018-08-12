package com.chesire.malime.mocks

import com.chesire.malime.INVALID_PASSWORD
import com.chesire.malime.INVALID_USERNAME
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.models.AuthModel
import io.reactivex.Single
import javax.inject.Inject

class MockAuthApi @Inject constructor() : AuthApi {
    override fun login(username: String, password: String): Single<AuthModel> {
        return Single.create {
            when {
                username == INVALID_USERNAME -> it.tryOnError(Throwable("Invalid username supplied"))
                password == INVALID_PASSWORD -> it.tryOnError(Throwable("Invalid password supplied"))
                else -> it.onSuccess(AuthModel("", "", 0, ""))
            }
        }
    }

    override fun getNewAuthToken(refreshToken: String): Single<AuthModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserId(): Single<Int> {
        return Single.just(1)
    }
}