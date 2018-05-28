package com.chesire.malime.kitsu

import io.reactivex.Observable
import timber.log.Timber


class KitsuManager(
    private val api: KitsuApi = KitsuApi()
) {
    fun login(username: String, password: String): Observable<Any> {
        return Observable.create {
            val callResponse = api.login(username, password)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Login successful")
            } else {
                Timber.e("Login failure")
            }
        }
    }
}