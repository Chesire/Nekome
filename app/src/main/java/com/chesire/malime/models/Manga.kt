package com.chesire.malime.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.chesire.malime.MalStates
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
@Root(name = "manga")
data class Manga(
    @PrimaryKey
    @field:Element(name = "series_mangadb_id", required = false)
    @param:Element(name = "series_mangadb_id", required = false)
    var seriesMangaDbId: Int? = null,

    @field:Element(name = "series_title", required = false)
    @param:Element(name = "series_title", required = false)
    var seriesTitle: String? = null,

    @field:Element(name = "series_synonyms", required = false)
    @param:Element(name = "series_synonyms", required = false)
    var seriesSynonyms: String? = null,

    @field:Element(name = "series_type", required = false)
    @param:Element(name = "series_type", required = false)
    var seriesType: Int? = null,

    @field:Element(name = "series_chapters", required = false)
    @param:Element(name = "series_chapters", required = false)
    var seriesChapters: Int? = null,

    @field:Element(name = "series_volumes", required = false)
    @param:Element(name = "series_volumes", required = false)
    var seriesVolumes: Int? = null,

    @field:Element(name = "series_status", required = false)
    @param:Element(name = "series_status", required = false)
    var seriesStatus: Int? = null,

    @Deprecated("Use seriesStartDate instead")
    @field:Element(name = "series_start", required = false)
    @param:Element(name = "series_start", required = false)
    var seriesStart: String? = null,

    @Deprecated("Use seriesEndDate instead")
    @field:Element(name = "series_end", required = false)
    @param:Element(name = "series_end", required = false)
    var seriesEnd: String? = null,

    @field:Element(name = "series_image", required = false)
    @param:Element(name = "series_image", required = false)
    var seriesImage: String? = null,

    @field:Element(name = "my_id", required = false)
    @param:Element(name = "my_id", required = false)
    var myId: Int? = null,

    @field:Element(name = "my_read_chapters", required = false)
    @param:Element(name = "my_read_chapters", required = false)
    var myReadChapters: Int? = null,

    @field:Element(name = "my_read_volumes", required = false)
    @param:Element(name = "my_read_volumes", required = false)
    var myReadVolumes: Int? = null,

    @field:Element(name = "my_start_date", required = false)
    @param:Element(name = "my_start_date", required = false)
    var myStartDate: String? = null,

    @field:Element(name = "my_finish_date", required = false)
    @param:Element(name = "my_finish_date", required = false)
    var myFinishDate: String? = null,

    @field:Element(name = "my_score", required = false)
    @param:Element(name = "my_score", required = false)
    var myScore: Int? = null,

    @field:Element(name = "my_status", required = false)
    @param:Element(name = "my_status", required = false)
    var myStatus: Int? = null,

    @field:Element(name = "my_rereadingg", required = false)
    @param:Element(name = "my_rereadingg", required = false)
    var myRereading: Int? = null,

    @field:Element(name = "my_rereading_chap", required = false)
    @param:Element(name = "my_rereading_chap", required = false)
    var myRereadingChap: Int? = null,

    @field:Element(name = "my_last_updated", required = false)
    @param:Element(name = "my_last_updated", required = false)
    var myLastUpdated: Int? = null,

    @field:Element(name = "my_tags", required = false)
    @param:Element(name = "my_tags", required = false)
    var myTags: String? = null
) : Parcelable {
    @Ignore
    private val baseUrl: String = "https://myanimelist.net/manga/"

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

    /**
     * Creates a new [Manga] object to temporarily store in room.
     */
    constructor(searchItem: Entry) : this(
        seriesMangaDbId = searchItem.id,
        seriesTitle = searchItem.title,
        seriesSynonyms = searchItem.synonyms,
        //seriesType = searchItem.type,
        seriesChapters = searchItem.chapters,
        seriesVolumes = searchItem.volumes,
        //seriesStatus = searchItem.status,
        seriesStart = searchItem.start_date,
        seriesEnd = searchItem.end_date,
        seriesImage = searchItem.image,
        myId = 0,
        myReadChapters = 0,
        myReadVolumes = 0,
        myStartDate = "",
        myFinishDate = "",
        myScore = 0,
        myStatus = MalStates.READING.id,
        myRereading = 0,
        myRereadingChap = 0,
        myLastUpdated = 0,
        myTags = ""
    )

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
        writeValue(myRereadingChap)
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