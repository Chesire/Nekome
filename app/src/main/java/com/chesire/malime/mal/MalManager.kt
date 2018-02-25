package com.chesire.malime.mal

import io.reactivex.Observable
import timber.log.Timber

class MalManager(
        username: String,
        password: String,
        private val api: MalApi = MalApi(username, password)
) {
    fun searchForAnime(name: String): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.searchForAnime(name)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.e("Success")
                subscriber.onComplete()
            } else {
                Timber.e("Error")
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}