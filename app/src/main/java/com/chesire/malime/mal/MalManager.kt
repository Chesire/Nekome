package com.chesire.malime.mal

import com.chesire.malime.models.Anime
import com.chesire.malime.models.Entry
import com.chesire.malime.models.Manga
import com.chesire.malime.models.MyInfo
import com.chesire.malime.models.UpdateAnime
import com.chesire.malime.models.UpdateManga
import io.reactivex.Observable
import timber.log.Timber

/**
 * Provides a manager to interact with the MyAnimeList API.
 */
class MalManager(
    auth: String,
    private val api: MalApi = MalApi(auth)
) {
    /**
     * Adds a specific anime series with all data in [anime].
     *
     * @param anime model containing data about the specified series
     * @return [Observable] instance that has success and error states
     */
    fun addAnime(anime: UpdateAnime): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.addAnime(anime.id, anime.getXml())
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Anime [%s] has added", anime.title)
                subscriber.onNext(Any())
                subscriber.onComplete()
            } else {
                Timber.e(
                    Throwable(response.message()),
                    "Error adding anime [%s] - %s",
                    anime.title,
                    response.errorBody()
                )
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Adds a specific manga series with all data in [manga].
     *
     * @param manga model containing data about the specified series
     * @return [Observable] instance that has success and error states
     */
    fun addManga(manga: UpdateManga): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.addManga(manga.id, manga.getXml())
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Manga [%s] has added", manga.title)
                subscriber.onNext(Any())
                subscriber.onComplete()
            } else {
                Timber.e(
                    Throwable(response.message()),
                    "Error adding manga [%s] - %s",
                    manga.title,
                    response.errorBody()
                )
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Request all anime for a user.
     *
     * @param username of the user to get the anime for
     * @return [Observable] instance containing a [Pair] of a [MyInfo] and all found anime
     */
    fun getAllAnime(username: String): Observable<Pair<MyInfo, List<Anime>?>> {
        return Observable.create { subscriber ->
            val callResponse = api.getAllAnime(username)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Get all anime successful")

                val responseBody = response.body()
                if (responseBody?.myInfo == null) {
                    subscriber.tryOnError(Throwable(response.message()))
                } else {
                    subscriber.onNext(Pair(responseBody.myInfo, responseBody.animeList))
                    subscriber.onComplete()
                }
            } else {
                Timber.e(Throwable(response.message()))
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Request all manga for a user.
     *
     * @param username of the user to get the manga for
     * @return [Observable] instance containing a [Pair] of a [MyInfo] and all found manga
     */
    fun getAllManga(username: String): Observable<Pair<MyInfo, List<Manga>?>> {
        return Observable.create { subscriber ->
            val callResponse = api.getAllManga(username)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Get all manga successful")

                val responseBody = response.body()
                if (responseBody?.myInfo == null) {
                    subscriber.tryOnError(Throwable(response.message()))
                } else {
                    subscriber.onNext(Pair(responseBody.myInfo, responseBody.mangaList))
                    subscriber.onComplete()
                }
            } else {
                Timber.e(Throwable(response.message()))
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Verifies a users credentials.
     * <p>
     * Since there is no "login" or auth token to store, this just verifies that the credentials
     * entered are correct.
     *
     * @return [Observable] with the success and failure states
     */
    fun loginToAccount(): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.loginToAccount()
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Login method successful")
                subscriber.onNext(Any())
                subscriber.onComplete()
            } else {
                Timber.e(
                    Throwable(response.message()),
                    "Error with the login method - %s",
                    response.errorBody()
                )
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Executes a search for the anime [name].
     *
     * @param name of the anime to find
     * @return [Observable] instance containing a list of all found anime
     */
    fun searchForAnime(name: String): Observable<List<Entry>> {
        return Observable.create { subscriber ->
            val callResponse = api.searchForAnime(name)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody == null) {
                    subscriber.tryOnError(Throwable(response.message()))
                } else {
                    subscriber.onNext(responseBody.entries)
                    subscriber.onComplete()
                }
            } else {
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Executes a search for the manga [name].
     *
     * @param name of the manga to find
     * @return [Observable] instance containing a list of all found manga
     */
    fun searchForManga(name: String): Observable<List<Entry>> {
        return Observable.create { subscriber ->
            val callResponse = api.searchForManga(name)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody == null) {
                    subscriber.tryOnError(Throwable(response.message()))
                } else {
                    subscriber.onNext(responseBody.entries)
                    subscriber.onComplete()
                }
            } else {
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Updates a specific anime series with all data in [anime].
     *
     * @param anime model containing all updates to the specified series
     * @return [Observable] instance that has success and error states
     */
    fun updateAnime(anime: UpdateAnime): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.updateAnime(anime.id, anime.getXml())
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Anime [%s] has updated to episode [%d]", anime.title, anime.episode)
                subscriber.onNext(Any())
                subscriber.onComplete()
            } else {
                Timber.e(
                    Throwable(response.message()),
                    "Error Updating anime [%s] - %s",
                    anime.title,
                    response.errorBody()
                )
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }

    /**
     * Updates a specific manga series with all data in [manga].
     *
     * @param manga model containing all updates to the specified series
     * @return [Observable] instance that has success and error states
     */
    fun updateManga(manga: UpdateManga): Observable<Any> {
        return Observable.create { subscriber ->
            val callResponse = api.updateManga(manga.id, manga.getXml())
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Manga [%s] has updated to episode [%d]", manga.title, manga.chapter)
                subscriber.onNext(Any())
                subscriber.onComplete()
            } else {
                Timber.e(
                    Throwable(response.message()),
                    "Error Updating manga [%s] - %s",
                    manga.title,
                    response.errorBody()
                )
                subscriber.tryOnError(Throwable(response.message()))
            }
        }
    }
}