package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.LoginResponse
import io.reactivex.Single
import timber.log.Timber

class KitsuManager {
    private lateinit var api: KitsuApi

    fun login(username: String, password: String): Single<LoginResponse> {
        return Single.create {
            val authApi = KitsuApi("")
            val callResponse = authApi.login(username, password)
            val response = callResponse.execute()

            if (response.isSuccessful && response.body() != null) {
                Timber.i("Login successful")

                response.body().let { responseObject ->
                    api = KitsuApi(responseObject!!.accessToken)
                    it.onSuccess(responseObject)
                }
            } else {
                Timber.e(Throwable(response.message()), "Error logging in")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }
}