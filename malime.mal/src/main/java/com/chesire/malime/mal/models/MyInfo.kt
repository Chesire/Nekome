package com.chesire.malime.mal.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "myinfo")
data class MyInfo(
    @field:Element(name = "user_id", required = false)
    @param:Element(name = "user_id", required = false)
    val userId: Int? = null,

    @field:Element(name = "user_name", required = false)
    @param:Element(name = "user_name", required = false)
    val userName: String? = null,

    @field:Element(name = "user_reading", required = false)
    @param:Element(name = "user_reading", required = false)
    val userReading: Int? = null,

    @field:Element(name = "user_watching", required = false)
    @param:Element(name = "user_watching", required = false)
    val userWatching: Int? = null,

    @field:Element(name = "user_completed", required = false)
    @param:Element(name = "user_completed", required = false)
    val userCompleted: Int? = null,

    @field:Element(name = "user_onhold", required = false)
    @param:Element(name = "user_onhold", required = false)
    val userOnHold: Int? = null,

    @field:Element(name = "user_dropped", required = false)
    @param:Element(name = "user_dropped", required = false)
    val userDropped: Int? = null,

    @field:Element(name = "user_plantoread", required = false)
    @param:Element(name = "user_plantoread", required = false)
    val userPlanToRead: Int? = null,

    @field:Element(name = "user_plantowatch", required = false)
    @param:Element(name = "user_plantowatch", required = false)
    val userPlanToWatch: Int? = null,

    @field:Element(name = "user_days_spent_watching", required = false)
    @param:Element(name = "user_days_spent_watching", required = false)
    val userDaysSpentWatching: Float? = null
)