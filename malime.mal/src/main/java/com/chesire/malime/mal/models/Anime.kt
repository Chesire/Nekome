package com.chesire.malime.mal.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "anime")
data class Anime(
    @field:Element(name = "series_animedb_id", required = false)
    @param:Element(name = "series_animedb_id", required = false)
    var seriesAnimeDbId: Int? = null,

    @field:Element(name = "series_title", required = false)
    @param:Element(name = "series_title", required = false)
    var seriesTitle: String? = null,

    @field:Element(name = "series_synonyms", required = false)
    @param:Element(name = "series_synonyms", required = false)
    var seriesSynonyms: String? = null,

    @field:Element(name = "series_type", required = false)
    @param:Element(name = "series_type", required = false)
    var seriesType: Int? = null,

    @field:Element(name = "series_episodes", required = false)
    @param:Element(name = "series_episodes", required = false)
    var seriesEpisodes: Int? = null,

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

    @field:Element(name = "my_watched_episodes", required = false)
    @param:Element(name = "my_watched_episodes", required = false)
    var myWatchedEpisodes: Int? = null,

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

    @field:Element(name = "my_rewatching", required = false)
    @param:Element(name = "my_rewatching", required = false)
    var myRewatching: Int? = null,

    @field:Element(name = "my_rewatching_ep", required = false)
    @param:Element(name = "my_rewatching_ep", required = false)
    var myRewatchingEp: Int? = null,

    @field:Element(name = "my_last_updated", required = false)
    @param:Element(name = "my_last_updated", required = false)
    var myLastUpdated: Int? = null,

    @field:Element(name = "my_tags", required = false)
    @param:Element(name = "my_tags", required = false)
    var myTags: String? = null
)
