package com.chesire.malime.mal.api

import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.models.LoginResponse
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.mal.models.Anime
import com.chesire.malime.mal.models.Entry
import com.chesire.malime.mal.models.Manga
import com.chesire.malime.mal.models.MyInfo
import com.chesire.malime.mal.models.UpdateAnime
import com.chesire.malime.mal.models.UpdateManga
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

/**
 * Provides a manager to interact with the MyAnimeList API.
 */
class MalManager(
    private val api: MalApi,
    private val username: String
) : MalimeApi {
    /**
     * Verifies a users credentials.
     * <p>
     * Since there is no "login" or auth token to store, this just verifies that the credentials
     * entered are correct.
     *
     * @return [Single<LoginResponse>] with the success and failure states, will be an empty LoginResponse
     */
    override fun login(username: String, password: String): Single<LoginResponse> {
        return Single.create {
            val callResponse = api.loginToAccount()
            val response = callResponse.execute()

            if (response.isSuccessful) {
                Timber.i("Login successful")
                it.onSuccess(LoginResponse("", ""))
            } else {
                Timber.e(
                    Throwable(response.message()),
                    "Error with the login method - %s",
                    response.errorBody()
                )
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun getUserId(username: String): Single<Int> {
        throw NotImplementedError("Don't need this for Malime")
    }

    override fun getUserLibrary(): Observable<List<MalimeModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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
     * @return [Observable] instance containing a [Pair] of a [MyInfo] and all found anime
     */
    fun getAllAnime(): Observable<Pair<MyInfo, List<Anime>?>> {
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
     * @return [Observable] instance containing a [Pair] of a [MyInfo] and all found manga
     */
    fun getAllManga(): Observable<Pair<MyInfo, List<Manga>?>> {
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