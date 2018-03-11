package com.chesire.malime.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "anime")
data class Anime(
        @field:Element(name = "series_animedb_id", required = false)
        @param:Element(name = "series_animedb_id", required = false)
        val series_animedb_id: Int? = null,

        @field:Element(name = "series_title", required = false)
        @param:Element(name = "series_title", required = false)
        val series_title: String? = null,

        @field:Element(name = "series_synonyms", required = false)
        @param:Element(name = "series_synonyms", required = false)
        val series_synonyms: String? = null,

        @field:Element(name = "series_type", required = false)
        @param:Element(name = "series_type", required = false)
        val series_type: Int? = null,

        @field:Element(name = "series_episodes", required = false)
        @param:Element(name = "series_episodes", required = false)
        val series_episodes: Int? = null,

        @field:Element(name = "series_status", required = false)
        @param:Element(name = "series_status", required = false)
        val series_status: Int? = null,

        @field:Element(name = "series_start", required = false)
        @param:Element(name = "series_start", required = false)
        val series_start: String? = null,

        @field:Element(name = "series_end", required = false)
        @param:Element(name = "series_end", required = false)
        val series_end: String? = null,

        @field:Element(name = "series_image", required = false)
        @param:Element(name = "series_image", required = false)
        val series_image: String? = null,

        @field:Element(name = "my_id", required = false)
        @param:Element(name = "my_id", required = false)
        val my_id: Int? = null,

        @field:Element(name = "my_watched_episodes", required = false)
        @param:Element(name = "my_watched_episodes", required = false)
        val my_watched_episodes: Int? = null,

        @field:Element(name = "my_start_date", required = false)
        @param:Element(name = "my_start_date", required = false)
        val my_start_date: String? = null,

        @field:Element(name = "my_finish_date", required = false)
        @param:Element(name = "my_finish_date", required = false)
        val my_finish_date: String? = null,

        @field:Element(name = "my_score", required = false)
        @param:Element(name = "my_score", required = false)
        val my_score: Int? = null,

        @field:Element(name = "my_status", required = false)
        @param:Element(name = "my_status", required = false)
        val my_status: Int? = null,

        @field:Element(name = "my_rewatching", required = false)
        @param:Element(name = "my_rewatching", required = false)
        val my_rewatching: Int? = null,

        @field:Element(name = "my_rewatching_ep", required = false)
        @param:Element(name = "my_rewatching_ep", required = false)
        val my_rewatching_ep: Int? = null,

        @field:Element(name = "my_last_updated", required = false)
        @param:Element(name = "my_last_updated", required = false)
        val my_last_updated: Int? = null,

        @field:Element(name = "my_tags", required = false)
        @param:Element(name = "my_tags", required = false)
        val my_tags: String? = null
)