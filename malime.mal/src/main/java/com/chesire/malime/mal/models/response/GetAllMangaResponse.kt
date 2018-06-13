package com.chesire.malime.mal.models.response

import com.chesire.malime.mal.models.Manga
import com.chesire.malime.mal.models.MyInfo
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "myanimelist")
data class GetAllMangaResponse(
    @field:Element(name = "myinfo", required = false)
    @param:Element(name = "myinfo", required = false)
    val myInfo: MyInfo? = null,

    @field:ElementList(inline = true, entry = "manga", required = false)
    @param:ElementList(inline = true, entry = "manga", required = false)
    val mangaList: List<Manga>? = null
)