package com.chesire.malime.kitsu.api

class KitsuManagerFactory {
    fun get(accessToken: String = "", userId: Int = 0): KitsuManager {
        return KitsuManager(
            KitsuApi(accessToken),
            userId
        )
    }
}