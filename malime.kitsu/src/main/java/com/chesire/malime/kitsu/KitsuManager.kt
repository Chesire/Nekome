package com.chesire.malime.kitsu

import com.chesire.malime.kitsu.models.LoginResponse
import io.reactivex.Observable
import timber.log.Timber

class KitsuManager {
    private lateinit var api: KitsuApi

    fun login(username: String, password: String): Observable<LoginResponse> {
        return Observable.create {
            val authApi = KitsuApi("")
            val callResponse = authApi.login(username, password)
            val response = callResponse.execute()

            if (response.isSuccessful && response.body() != null) {
                Timber.i("Login successful")
                response.body().let { responseObject ->
                    api = KitsuApi(responseObject!!.accessToken)
                    it.onNext(responseObject)
                }

                it.onComplete()
            } else {
                Timber.e(
                    Throwable(response.message()),
                    "Error logging in"
                )
                it.tryOnError(Throwable(response.message()))
            }
        }
    }
}