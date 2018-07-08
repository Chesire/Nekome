package com.chesire.malime.kitsu.models

import com.google.gson.annotations.SerializedName

data class AttributesItem(
    @SerializedName("slug")
    val slug: String,
    @SerializedName("canonicalTitle")
    val canonicalTitle: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("subtype")
    val subtype: String,
    @SerializedName("posterImage")
    val posterImage: Map<String, Any>?,
    @SerializedName("coverImage")
    val coverImage: Map<String, Any>?,
    @SerializedName("episodeCount")
    val episodeCount: Int,
    @SerializedName("chapterCount")
    val chapterCount: Int,
    @SerializedName("nsfw")
    val nsfw: Boolean,
    @SerializedName("startedAt")
    val startedAt: String?,
    @SerializedName("finishedAt")
    val finishedAt: String?
)