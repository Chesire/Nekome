package com.chesire.malime.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "myinfo")
data class MyInfo(
        @field:Element(name = "user_id", required = false)
        @param:Element(name = "user_id", required = false)
        val user_id: Int? = null,

        @field:Element(name = "user_name", required = false)
        @param:Element(name = "user_name", required = false)
        val user_name: String? = null,

        @field:Element(name = "user_reading", required = false)
        @param:Element(name = "user_reading", required = false)
        val user_reading: Int? = null,

        @field:Element(name = "user_completed", required = false)
        @param:Element(name = "user_completed", required = false)
        val user_completed: Int? = null,

        @field:Element(name = "user_onhold", required = false)
        @param:Element(name = "user_onhold", required = false)
        val user_onhold: Int? = null,

        @field:Element(name = "user_dropped", required = false)
        @param:Element(name = "user_dropped", required = false)
        val user_dropped: Int? = null,

        @field:Element(name = "user_plantoread", required = false)
        @param:Element(name = "user_plantoread", required = false)
        val user_plantoread: Int? = null,

        @field:Element(name = "user_days_spent_watching", required = false)
        @param:Element(name = "user_days_spent_watching", required = false)
        val user_days_spent_watching: Float? = null
)