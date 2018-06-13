package com.chesire.malime.mal.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

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

    @field:Element(name = "series_start", required = false)
    @param:Element(name = "series_start", required = false)
    var seriesStart: String? = null,

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
)