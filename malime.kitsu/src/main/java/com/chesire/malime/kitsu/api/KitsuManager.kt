package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.models.KitsuItem
import com.chesire.malime.kitsu.models.LibraryResponse
import com.chesire.malime.kitsu.models.LoginResponse
import io.reactivex.Single
import timber.log.Timber

class KitsuManager(
    private val api: KitsuApi,
    private val userId: Int
) {
    fun login(username: String, password: String): Single<LoginResponse> {
        // The api mentions it wants the username, but it seems it wants the email address instead
        return Single.create {
            val callResponse = api.login(username, password)
            val response = callResponse.execute()

            if (response.isSuccessful && response.body() != null) {
                Timber.i("Login successful")

                response.body().let { responseObject ->
                    it.onSuccess(responseObject!!)
                }
            } else {
                Timber.e(Throwable(response.message()), "Error logging in")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    fun getUserId(username: String): Single<Int> {
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

    fun getItems(): Single<List<KitsuItem>> {
        return Single.create {
            val callResponse = api.getItems(userId)
            val response = callResponse.execute()
            val body = response.body()

            if (response.isSuccessful && body != null && body.data.isNotEmpty()) {
                Timber.i("Items found")

                val myTitleData = body.data
                val fullTitleData = body.included

                val items = myTitleData.zip(fullTitleData, { user, full ->
                    // Items should be married up by their index
                    KitsuItem(
                        seriesId = full.id,
                        userSeriesId = user.id,
                        type = full.type,
                        slug = full.attributes.slug,
                        canonicalTitle = full.attributes.canonicalTitle,
                        seriesStatus = full.attributes.status,
                        userSeriesStatus = user.attributes.status,
                        progress = user.attributes.progress,
                        episodeCount = full.attributes.episodeCount,
                        chapterCount = full.attributes.chapterCount,
                        nsfw = full.attributes.nsfw
                    )
                })

                it.onSuccess(items)
            } else {
                Timber.e(Throwable(response.message()), "Error getting the items")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }
}