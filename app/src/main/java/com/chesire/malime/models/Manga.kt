package com.chesire.malime.models

import android.arch.persistence.room.Ignore
import android.os.Parcel
import android.os.Parcelable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Root(name = "manga")
data class Manga(
    @field:Element(name = "series_mangadb_id", required = false)
    @param:Element(name = "series_mangadb_id", required = false)
    val seriesMangaDbId: Int? = null,

    @field:Element(name = "series_title", required = false)
    @param:Element(name = "series_title", required = false)
    val seriesTitle: String? = null,

    @field:Element(name = "series_synonyms", required = false)
    @param:Element(name = "series_synonyms", required = false)
    val seriesSynonyms: String? = null,

    @field:Element(name = "series_type", required = false)
    @param:Element(name = "series_type", required = false)
    val seriesType: Int? = null,

    @field:Element(name = "series_chapters", required = false)
    @param:Element(name = "series_chapters", required = false)
    val seriesChapters: Int? = null,

    @field:Element(name = "series_volumes", required = false)
    @param:Element(name = "series_volumes", required = false)
    val seriesVolumes: Int? = null,

    @field:Element(name = "series_status", required = false)
    @param:Element(name = "series_status", required = false)
    val seriesStatus: Int? = null,

    @Deprecated("Use seriesStartDate instead")
    @field:Element(name = "series_start", required = false)
    @param:Element(name = "series_start", required = false)
    val seriesStart: String? = null,

    @Deprecated("Use seriesEndDate instead")
    @field:Element(name = "series_end", required = false)
    @param:Element(name = "series_end", required = false)
    val seriesEnd: String? = null,

    @field:Element(name = "series_image", required = false)
    @param:Element(name = "series_image", required = false)
    val seriesImage: String? = null,

    @field:Element(name = "my_id", required = false)
    @param:Element(name = "my_id", required = false)
    val myId: Int? = null,

    @field:Element(name = "my_read_chapters", required = false)
    @param:Element(name = "my_read_chapters", required = false)
    var myReadChapters: Int? = null,

    @field:Element(name = "my_read_volumes", required = false)
    @param:Element(name = "my_read_volumes", required = false)
    var myReadVolumes: Int? = null,

    @field:Element(name = "my_start_date", required = false)
    @param:Element(name = "my_start_date", required = false)
    val myStartDate: String? = null,

    @field:Element(name = "my_finish_date", required = false)
    @param:Element(name = "my_finish_date", required = false)
    val myFinishDate: String? = null,

    @field:Element(name = "my_score", required = false)
    @param:Element(name = "my_score", required = false)
    val myScore: Int? = null,

    @field:Element(name = "my_status", required = false)
    @param:Element(name = "my_status", required = false)
    var myStatus: Int? = null,

    @field:Element(name = "my_rereadingg", required = false)
    @param:Element(name = "my_rereadingg", required = false)
    val myRereading: Int? = null,

    @field:Element(name = "my_rereading_chap", required = false)
    @param:Element(name = "my_rereading_chap", required = false)
    val myRewatchingChap: Int? = null,

    @field:Element(name = "my_last_updated", required = false)
    @param:Element(name = "my_last_updated", required = false)
    val myLastUpdated: Int? = null,

    @field:Element(name = "my_tags", required = false)
    @param:Element(name = "my_tags", required = false)
    val myTags: String? = null
) : Parcelable {
    @Ignore
    private val baseUrl: String = "https://myanimelist.net/anime/"

    fun getMalUrl(): String = baseUrl + seriesMangaDbId

    fun getTotalChapters(): String =
        if (seriesChapters == 0) {
            "??"
        } else {
            seriesChapters.toString()
        }

    fun getSeriesStartDate(): Date = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(seriesStart)

    fun getSeriesEndDate(): Date {
        val dateToUse: String = if (seriesEnd == "0000-00-00") {
            "9999-99-99"
        } else {
            seriesEnd!!
        }

        return SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(dateToUse)
    }

    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seriesMangaDbId)
        writeString(seriesTitle)
        writeString(seriesSynonyms)
        writeValue(seriesType)
        writeValue(seriesChapters)
        writeValue(seriesVolumes)
        writeValue(seriesStatus)
        writeString(seriesStart)
        writeString(seriesEnd)
        writeString(seriesImage)
        writeValue(myId)
        writeValue(myReadChapters)
        writeValue(myReadVolumes)
        writeString(myStartDate)
        writeString(myFinishDate)
        writeValue(myScore)
        writeValue(myStatus)
        writeValue(myRereading)
        writeValue(myRewatchingChap)
        writeValue(myLastUpdated)
        writeString(myTags)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Manga> = object : Parcelable.Creator<Manga> {
            override fun createFromParcel(source: Parcel): Manga = Manga(source)
            override fun newArray(size: Int): Array<Manga?> = arrayOfNulls(size)
        }
    }
}