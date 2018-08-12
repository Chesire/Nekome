package com.chesire.malime.mocks

import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.models.AuthModel
import io.reactivex.Single
import javax.inject.Inject

class MockAuthApi @Inject constructor() : AuthApi {
    override fun login(username: String, password: String): Single<AuthModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNewAuthToken(refreshToken: String): Single<AuthModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserId(): Single<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}