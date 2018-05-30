package com.chesire.malime.kitsu

import com.chesire.malime.kitsu.api.KitsuManager

class KitsuManagerFactory {
    fun get(): KitsuManager {
        return KitsuManager()
    }
}