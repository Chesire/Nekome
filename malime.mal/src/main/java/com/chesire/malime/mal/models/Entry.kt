package com.chesire.malime.mal.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "entry")
data class Entry(
    @field:Element(name = "id", required = false)
    @param:Element(name = "id", required = false)
    val id: Int? = null,

    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    val title: String? = null,

    @field:Element(name = "english", required = false)
    @param:Element(name = "english", required = false)
    val english: String? = null,

    @field:Element(name = "synonyms", required = false)
    @param:Element(name = "synonyms", required = false)
    val synonyms: String? = null,

    @field:Element(name = "episodes", required = false)
    @param:Element(name = "episodes", required = false)
    val episodes: Int? = null,

    @field:Element(name = "chapters", required = false)
    @param:Element(name = "chapters", required = false)
    val chapters: Int? = null,

    @field:Element(name = "volumes", required = false)
    @param:Element(name = "volumes", required = false)
    val volumes: Int? = null,

    @field:Element(name = "score", required = false)
    @param:Element(name = "score", required = false)
    val score: Double? = null,

    @field:Element(name = "type", required = false)
    @param:Element(name = "type", required = false)
    val type: String? = null,

    @field:Element(name = "status", required = false)
    @param:Element(name = "status", required = false)
    val status: String? = null,

    @field:Element(name = "start_date", required = false)
    @param:Element(name = "start_date", required = false)
    val start_date: String? = null,

    @field:Element(name = "end_date", required = false)
    @param:Element(name = "end_date", required = false)
    val end_date: String? = null,

    @field:Element(name = "synopsis", required = false)
    @param:Element(name = "synopsis", required = false)
    val synopsis: String? = null,

    @field:Element(name = "image", required = false)
    @param:Element(name = "image", required = false)
    val image: String? = null
)