package com.chesire.malime.mal.models.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "user")
data class LoginToAccountResponse(
    @field:Element(name = "id", required = false)
    @param:Element(name = "id", required = false)
    val id: Int? = null,

    @field:Element(name = "username", required = false)
    @param:Element(name = "username", required = false)
    val username: String? = null
)
