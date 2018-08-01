package com.chesire.malime.kitsu.api

import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.kitsu.models.response.UpdateItemResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

private const val MAX_RETRIES = 3

class KitsuManager @Inject constructor(
    private val api: KitsuApi,
    authorizer: KitsuAuthorizer
) : AuthApi, LibraryApi, SearchApi {
    private val userId = authorizer.retrieveUser()

    override fun login(username: String, password: String): Single<AuthModel> {
        // The api mentions it wants the username, but it seems it wants the email address instead
        return Single.create {
            val callResponse = api.login(username, password)
            val response = callResponse.execute()

            if (response.isSuccessful && response.body() != null) {
                Timber.i("Login successful")

                response.body()?.let { responseObject ->
                    it.onSuccess(
                        AuthModel(
                            responseObject.accessToken,
                            responseObject.refreshToken,
                            responseObject.createdAt + responseObject.expiresIn,
                            "kitsu"
                        )
                    )
                }
            } else {
                Timber.e(Throwable(response.message()), "Error logging in")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun getNewAuthToken(refreshToken: String): Single<AuthModel> {
        return Single.create {
            val callResponse = api.refreshAuthToken(refreshToken)
            val response = callResponse.execute()

            if (response.isSuccessful && response.body() != null) {
                Timber.i("Successfully refreshed auth token")

                response.body()?.let { responseObject ->
                    it.onSuccess(
                        AuthModel(
                            responseObject.accessToken,
                            responseObject.refreshToken,
                            responseObject.createdAt + responseObject.expiresIn,
                            "kitsu"
                        )
                    )
                }
            } else {
                Timber.e(Throwable(response.message()), "Error regenerating auth token")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun getUserId(): Single<Int> {
        return Single.create {
            val callResponse = api.getUser()
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
        // Execute get anime and get manga parallel
        return Observable
            .just(
                getUserEntriesForType(ItemType.Anime),
                getUserEntriesForType(ItemType.Manga)
            )
            .flatMap {
                it.subscribeOn(Schedulers.computation())
            }
    }

    private fun getUserEntriesForType(type: ItemType): Observable<List<MalimeModel>> {
        return Observable.create {
            var page = 0
            var offset = 0
            var retries = 0

            while (true) {
                Timber.i("Getting user $type from offset $offset")

                val callResponse = api.getUserLibrary(userId, offset, type)
                val response = callResponse.execute()
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    Timber.i("Found ${body.data.count()} items")
                    retries = 0

                    // If it contains the "next" link, there are more to get
                    val next = body.links["next"] ?: ""
                    val userTitleData = body.data
                    val fullTitleData = body.included

                    val items = userTitleData.zip(fullTitleData) { user, full ->
                        // Items should be married up by their index

                        MalimeModel(
                            seriesId = full.id,
                            userSeriesId = user.id,
                            type = type,
                            subtype = Subtype.getSubtypeForKitsuString(full.attributes.subtype),
                            slug = full.attributes.slug,
                            title = full.attributes.canonicalTitle,
                            seriesStatus = SeriesStatus.getStatusForKitsuString(full.attributes.status),
                            userSeriesStatus = UserSeriesStatus.getStatusForKitsuString(user.attributes.status),
                            progress = user.attributes.progress,
                            totalLength = if (type == ItemType.Anime) {
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
                    }

                    it.onNext(items)

                    if (next.isEmpty()) {
                        break
                    } else {
                        page++
                        offset = 200 * page
                    }
                } else {
                    Timber.e(
                        Throwable(response.message()),
                        "Error getting the items for type $type, on retry $retries/$MAX_RETRIES"
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
                val seriesData = body.included[0]
                val newItem = MalimeModel(
                    seriesId = seriesData.id,
                    userSeriesId = body.data.id,
                    type = ItemType.getTypeForString(seriesData.type),
                    subtype = Subtype.getSubtypeForKitsuString(seriesData.attributes.subtype),
                    slug = seriesData.attributes.slug,
                    title = seriesData.attributes.canonicalTitle,
                    seriesStatus = SeriesStatus.getStatusForKitsuString(seriesData.attributes.status),
                    userSeriesStatus = UserSeriesStatus.getStatusForKitsuString(body.data.attributes.status),
                    progress = body.data.attributes.progress,
                    totalLength = if (ItemType.getTypeForString(seriesData.type) == ItemType.Anime) {
                        seriesData.attributes.episodeCount
                    } else {
                        seriesData.attributes.chapterCount
                    },
                    posterImage = getImage(seriesData.attributes.posterImage),
                    coverImage = getImage(seriesData.attributes.coverImage),
                    nsfw = seriesData.attributes.nsfw,
                    startDate = seriesData.attributes.startedAt ?: "",
                    endDate = seriesData.attributes.finishedAt ?: ""
                )

                it.onSuccess(newItem)
            } else {
                Timber.e(Throwable(response.message()), "Error updating the series")
                it.tryOnError(Throwable(response.message()))
            }
        }
    }

    override fun deleteItem(item: MalimeModel): Single<MalimeModel> {
        return Single.create {
            val callResponse = api.deleteItem(item.userSeriesId)
            val response = callResponse.execute()

            // If the series is not found, its likely already deleted
            if (response.isSuccessful || response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                Timber.i("Successfully deleted series")
                it.onSuccess(item)
            } else {
                Timber.e(Throwable(response.message()), "Error deleting the series")
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

            val callResponse = api.updateItem(item.userSeriesId, requestBody)
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

            if (response.isSuccessful && body != null) {
                Timber.i("Successfully searched, found [${body.data.count()}] items")
                val items = body.data.map {
                    MalimeModel(
                        seriesId = it.id,
                        userSeriesId = 0,
                        type = ItemType.getTypeForString(it.type),
                        subtype = Subtype.getSubtypeForKitsuString(it.attributes.subtype),
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
        return JSONObject(
            mapOf(
                "data" to mapOf(
                    "type" to "libraryEntries",
                    "attributes" to mapOf(
                        "progress" to item.progress,
                        "status" to item.userSeriesStatus.kitsuString
                    ),
                    "relationships" to mapOf(
                        "user" to mapOf(
                            "data" to mapOf(
                                "type" to "users",
                                "id" to userId
                            )
                        ),
                        "media" to mapOf(
                            "data" to mapOf(
                                "type" to item.type.text,
                                "id" to item.seriesId
                            )
                        )
                    )
                )
            )
        ).toString()
    }

    private fun createUpdateModel(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): String {
        return JSONObject(
            mapOf(
                "data" to mapOf(
                    "id" to item.userSeriesId,
                    "type" to "libraryEntries",
                    "attributes" to mapOf(
                        "progress" to newProgress,
                        "status" to newStatus.kitsuString
                    )
                )
            )
        ).toString()
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
}