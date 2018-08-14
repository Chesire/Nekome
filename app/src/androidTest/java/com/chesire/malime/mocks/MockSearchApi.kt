package com.chesire.malime.mocks

import com.chesire.malime.INVALID_SEARCH
import com.chesire.malime.VALID_SEARCH_MULTIPLE_ITEMS
import com.chesire.malime.VALID_SEARCH_MULTIPLE_OBSERVABLE_CALLS
import com.chesire.malime.VALID_SEARCH_NO_ITEMS
import com.chesire.malime.VALID_SEARCH_SINGLE_ITEM
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import javax.inject.Inject

class MockSearchApi @Inject constructor() : SearchApi {
    override fun searchForSeriesWith(title: String, type: ItemType): Observable<List<MalimeModel>> {
        return Observable.create {
            when (title) {
                INVALID_SEARCH -> it.tryOnError(Throwable("Invalid search supplied"))
                VALID_SEARCH_NO_ITEMS -> {
                    it.onNext(listOf())
                    it.onComplete()
                }
                VALID_SEARCH_SINGLE_ITEM -> {
                    it.onNext(listOf(getMalimeModel()))
                    it.onComplete()
                }
                VALID_SEARCH_MULTIPLE_ITEMS -> {
                    it.onNext(
                        listOf(
                            getMalimeModel(seriesId = 0),
                            getMalimeModel(seriesId = 1),
                            getMalimeModel(seriesId = 2),
                            getMalimeModel(seriesId = 3),
                            getMalimeModel(seriesId = 4),
                            getMalimeModel(seriesId = 5),
                            getMalimeModel(seriesId = 6),
                            getMalimeModel(seriesId = 7),
                            getMalimeModel(seriesId = 8),
                            getMalimeModel(seriesId = 9),
                            getMalimeModel(seriesId = 10),
                            getMalimeModel(seriesId = 11)
                        )
                    )
                    it.onComplete()
                }
                VALID_SEARCH_MULTIPLE_OBSERVABLE_CALLS -> {
                    for (i in 0..10) {
                        it.onNext(listOf(getMalimeModel(seriesId = i)))
                    }
                    it.onComplete()
                }
            }
        }
    }

    private fun getMalimeModel(
        seriesId: Int = 0,
        userSeriesId: Int = 0,
        type: ItemType = ItemType.Unknown,
        subtype: Subtype = Subtype.Unknown,
        slug: String = "SERIES-SLUG",
        title: String = "SERIES-TITLE",
        seriesStatus: SeriesStatus = SeriesStatus.Unknown,
        userSeriesStatus: UserSeriesStatus = UserSeriesStatus.Unknown,
        progress: Int = 0,
        totalLength: Int = 0,
        posterImage: String = "",
        coverImage: String = "",
        nsfw: Boolean = false,
        startDate: String = "0000-00-00",
        endDate: String = "0000-00-00"
    ): MalimeModel {
        return MalimeModel(
            seriesId = seriesId,
            userSeriesId = userSeriesId,
            type = type,
            subtype = subtype,
            slug = slug,
            title = title,
            seriesStatus = seriesStatus,
            userSeriesStatus = userSeriesStatus,
            progress = progress,
            totalLength = totalLength,
            posterImage = posterImage,
            coverImage = coverImage,
            nsfw = nsfw,
            startDate = startDate,
            endDate = endDate
        )
    }
}