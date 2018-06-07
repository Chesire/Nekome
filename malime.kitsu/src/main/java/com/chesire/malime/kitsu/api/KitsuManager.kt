package com.chesire.malime.kitsu.api

import com.chesire.malime.core.ItemStatus
import com.chesire.malime.core.ItemType
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.models.LoginResponse
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

private const val MAX_RETRIES = 3

class KitsuManager(
    private val api: KitsuApi,
    private val userId: Int
) : MalimeApi {
    override fun login(username: String, password: String): Single<LoginResponse> {
        // The api mentions it wants the username, but it seems it wants the email address instead
        return Single.create {
            val callResponse = api.login(username, password)
            val response = callResponse.execute()

            if (response.isSuccessful && response.body() != null) {
                Timber.i("Login successful")

                response.body().let { responseObject ->
                    it.onSuccess(
                        LoginResponse(
                            responseObject!!.accessToken,
                            responseObject.refreshToken
                        )
                    )
                }
            } else {
                Timber.e(Throwable(response.message()), "Error logging in")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun getUserId(username: String): Single<Int> {
        return Single.create {
            val callResponse = api.getUser(username)
            val response = callResponse.execute()
            val body = response.body()

            if (response.isSuccessful && body != null && body.data.isNotEmpty()) {
                Timber.i("User found")
                it.onSuccess(body.data[0].id)
            } else {
                Timber.e(Throwable(response.message()), "Error getting the user")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun getUserLibrary(): Observable<List<MalimeModel>> {
        return Observable.create {
            var offset = 0
            var retries = 0

            while (true) {
                Timber.i("Getting user library from offset $offset")

                val callResponse = api.getUserLibrary(userId, offset)
                val response = callResponse.execute()
                val body = response.body()

                if (response.isSuccessful && body != null && body.data.isNotEmpty()) {
                    Timber.i("Got next set of user items")
                    retries = 0

                    // If it contains the "next" link, there are more to get
                    val next = body.links["next"] ?: ""
                    val userTitleData = body.data
                    val fullTitleData = body.included

                    val items = userTitleData.zip(fullTitleData, { user, full ->
                        // Items should be married up by their index
                        MalimeModel(
                            seriesId = full.id,
                            userSeriesId = user.id,
                            type = ItemType.getTypeForString(full.type),
                            slug = full.attributes.slug,
                            title = full.attributes.canonicalTitle,
                            seriesStatus = full.attributes.status,
                            userSeriesStatus = ItemStatus.getStatusForKitsuString(user.attributes.status),
                            progress = user.attributes.progress,
                            totalLength = if (ItemType.getTypeForString(full.type) == ItemType.Anime) {
                                full.attributes.episodeCount
                            } else {
                                full.attributes.chapterCount
                            },
                            nsfw = full.attributes.nsfw
                        )
                    })

                    it.onNext(items)

                    if (next.isEmpty()) {
                        break
                    } else {
                        offset = next.substring(next.lastIndexOf('=') + 1).toInt()
                    }
                } else {
                    Timber.e(
                        Throwable(response.message()),
                        "Error getting the items, on retry $retries/$MAX_RETRIES"
                    )

                    if (retries < MAX_RETRIES) {
                        retries++
                    } else {
                        it.tryOnError(Throwable(response.message()))
                        break
                    }
                }
            }

            it.onComplete()
        }
    }
}