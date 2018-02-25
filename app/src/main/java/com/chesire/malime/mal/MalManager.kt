package com.chesire.malime.mal

import io.reactivex.Observable
import timber.log.Timber


class MalManager(
        private val api: MalApi = MalApi()
) {
    fun searchForAnime(name: String): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.searchForAnime(name)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.d("Success")
                subscriber.onComplete()
            } else {
                Timber.e("Error")
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}