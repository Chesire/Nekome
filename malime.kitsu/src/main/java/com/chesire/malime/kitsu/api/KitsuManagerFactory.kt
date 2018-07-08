package com.chesire.malime.kitsu.api

class KitsuManagerFactory {
    fun get(authorizer: KitsuAuthorizer): KitsuManager {
        return KitsuManager(KitsuApi(authorizer), authorizer)
    }
}