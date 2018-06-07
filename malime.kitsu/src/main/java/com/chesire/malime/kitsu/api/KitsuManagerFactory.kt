package com.chesire.malime.kitsu.api

import com.chesire.malime.kitsu.api.KitsuApi
import com.chesire.malime.kitsu.api.KitsuManager

class KitsuManagerFactory {
    fun get(): KitsuManager {
        return KitsuManager(KitsuApi(""), 0)
    }
}