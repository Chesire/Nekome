package com.chesire.malime.mal

import com.chesire.malime.models.Entry
import io.reactivex.Observable

class MalManager(
        username: String,
        password: String,
        private val api: MalApi = MalApi(username, password)
) {
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