package com.chesire.malime.kitsu.api

import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.LoginResponse
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.kitsu.models.UpdateItemResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val MAX_RETRIES = 3

class KitsuManager(
    private val api: KitsuApi,
    private val userId: Int
) : MalimeApi, SearchApi {

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

    override fun getItemUrl(item: MalimeModel): String {
        return "$KitsuEndpoint${item.type.text}/${item.slug}"
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

                if (response.isSuccessful && body != null) {
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
                            seriesStatus = SeriesStatus.getStatusForKitsuString(full.attributes.status),
                            userSeriesStatus = UserSeriesStatus.getStatusForKitsuString(user.attributes.status),
                            progress = user.attributes.progress,
                            totalLength = if (ItemType.getTypeForString(full.type) == ItemType.Anime) {
                                full.attributes.episodeCount
                            } else {
                                full.attributes.chapterCount
                            },
                            posterImage = getImage(full.attributes.posterImage),
                            coverImage = getImage(full.attributes.coverImage),
                            nsfw = full.attributes.nsfw,
                            startDate = user.attributes.startedAt ?: "",
                            endDate = user.attributes.finishedAt ?: ""
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

    override fun addItem(item: MalimeModel): Single<MalimeModel> {
        return Single.create {
            val json = createNewModel(item)
            val requestBody = RequestBody.create(MediaType.parse("application/vnd.api+json"), json)

            val callResponse = api.addItem(requestBody)
            val response = callResponse.execute()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Timber.i("Successfully updated series")
                it.onSuccess(item)
            } else {
                Timber.e(Throwable(response.message()), "Error updating the series")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun updateItem(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): Single<MalimeModel> {
        return Single.create {
            val json = createUpdateModel(item, newProgress, newStatus)
            val requestBody = RequestBody.create(MediaType.parse("application/vnd.api+json"), json)

            val callResponse = api.updateItem(
                item.userSeriesId,
                requestBody
            )
            val response = callResponse.execute()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Timber.i("Successfully updated series")
                it.onSuccess(getUpdatedModel(item, body))
            } else {
                Timber.e(Throwable(response.message()), "Error updating the series")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun searchForSeriesWith(title: String, type: ItemType): Observable<List<MalimeModel>> {
        return Observable.create {
            val callResponse = api.search(title, type)
            val response = callResponse.execute()
            val body = response.body()

            if (response.isSuccessful && body != null && body.data.isNotEmpty()) {
                Timber.i("Successfully searched, found [${body.data.count()}] items")
                val items = body.data.map {
                    MalimeModel(
                        seriesId = it.id,
                        userSeriesId = 0,
                        type = ItemType.getTypeForString(it.type),
                        slug = it.attributes.slug,
                        title = it.attributes.canonicalTitle,
                        seriesStatus = SeriesStatus.getStatusForKitsuString(it.attributes.status),
                        userSeriesStatus = UserSeriesStatus.Unknown,
                        progress = 0,
                        totalLength = if (ItemType.getTypeForString(it.type) == ItemType.Anime) {
                            it.attributes.episodeCount
                        } else {
                            it.attributes.chapterCount
                        },
                        posterImage = getImage(it.attributes.posterImage),
                        coverImage = getImage(it.attributes.coverImage),
                        nsfw = it.attributes.nsfw,
                        startDate = "",
                        endDate = ""
                    )
                }
                it.onNext(items)
                it.onComplete()
            } else {
                Timber.e(Throwable(response.message()), "Error performing search")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    private fun getImage(map: Map<String, Any>?): String {
        if (map == null) {
            return ""
        }
        return map["large"] as String?
                ?: map["medium"] as String?
                ?: map["original"] as String?
                ?: map["small"] as String?
                ?: map["tiny"] as String?
                ?: ""
    }

    private fun createNewModel(item: MalimeModel): String {
        return JsonObject().apply {
            add("data", JsonObject().apply {
                addProperty("type", "libraryEntries")
                add("attributes", JsonObject().apply {
                    addProperty("progress", 0)
                    addProperty("status", UserSeriesStatus.Current.kitsuString)
                })
                add("relationships", JsonObject().apply {
                    add("user", JsonObject().apply {
                        add("data", JsonObject().apply {
                            addProperty("type", "users")
                            addProperty("id", userId)
                        })
                    })
                    add("media", JsonObject().apply {
                        add("data", JsonObject().apply {
                            addProperty("type", item.type.text)
                            addProperty("id", item.seriesId)
                        })
                    })
                })
            })
        }.toString()
    }

    private fun createUpdateModel(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): String {
        val currentTime = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time)

        return JsonObject().apply {
            add("data", JsonObject().apply {
                addProperty("id", item.userSeriesId)
                addProperty("type", "libraryEntries")
                add("attributes", JsonObject().apply {
                    /*
                    addProperty(
                        "startedAt",
                        if (item.progress == 0 && newProgress > 0) {
                            currentTime
                        } else {
                            item.startDate
                        }
                    )
                    addProperty(
                        "finishedAt",
                        if (newProgress == item.totalLength) {
                            currentTime
                        } else {
                            item.endDate
                        }
                    )
                    */
                    addProperty("progress", newProgress)
                    addProperty("status", newStatus.kitsuString)
                })
            })
        }.toString()
    }

    private fun getUpdatedModel(
        originalItem: MalimeModel,
        updateItem: UpdateItemResponse
    ): MalimeModel {
        return originalItem.copy().apply {
            progress = updateItem.data.attributes.progress
            userSeriesStatus =
                    UserSeriesStatus.getStatusForKitsuString(updateItem.data.attributes.status)
        }
    }

    private fun getError(error: ResponseBody?) {
        try {
            val jObjError = JSONObject(error!!.string())
            val s = ""
        } catch (e: Exception) {
        }
    }
}