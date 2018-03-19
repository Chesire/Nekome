package com.chesire.malime.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

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

    @field:Element(name = "series_start", required = false)
    @param:Element(name = "series_start", required = false)
    val seriesStart: String? = null,

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
    val myReadChapters: Int? = null,

    @field:Element(name = "my_read_volumes", required = false)
    @param:Element(name = "my_read_volumes", required = false)
    val myReadVolumes: Int? = null,

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
    val myStatus: Int? = null,

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
)