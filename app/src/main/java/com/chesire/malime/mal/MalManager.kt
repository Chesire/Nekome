package com.chesire.malime.mal

import com.chesire.malime.models.Anime
import com.chesire.malime.models.Entry
import com.chesire.malime.models.Manga
import io.reactivex.Observable
import timber.log.Timber

class MalManager(
        auth: String,
        private val api: MalApi = MalApi(auth)
) {
    fun getAllAnime(username: String): Observable<List<Anime>> {
        return Observable.create { subscriber ->
            val callResponse = api.getAllAnime(username)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Get all anime successful")
            } else {
                Timber.e(Throwable(response.message()))
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    fun getAllManga(username: String): Observable<List<Manga>> {
        return Observable.create { subscriber ->
            val callResponse = api.getAllManga(username)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Get all manga successful")
            } else {
                Timber.e(Throwable(response.message()))
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    fun loginToAccount(): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.loginToAccount()
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Login method successful")
                subscriber.onNext(Any())
                subscriber.onComplete()
            } else {
                Timber.e(Throwable(response.message()), "Error with the login method - %s", response.errorBody())
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    fun searchForAnime(name: String): Observable<List<Entry>> {
        return Observable.create { subscriber ->
            val callResponse = api.searchForAnime(name)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody == null) {
                    subscriber.onError(Throwable(response.message()))
                } else {
                    val entries = responseBody.entries.map {
                        Entry(it.id, it.title, it.english, it.synonyms, it.episodes, it.score, it.type, it.status, it.start_date, it.end_date, it.synopsis, it.image)
                    }
                    subscriber.onNext(entries)
                    subscriber.onComplete()
                }
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}