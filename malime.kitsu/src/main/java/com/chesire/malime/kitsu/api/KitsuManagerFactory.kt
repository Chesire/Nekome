package com.chesire.malime.kitsu.api

import com.chesire.malime.core.api.AuthHandler

class KitsuManagerFactory {
    private val managers = HashMap<Int, KitsuManager>()

    fun get(authHandler: AuthHandler, userId: Int = 0): KitsuManager {
        // remove the manager at user id 0, as its the default used to login
        // sorry person with userId of 0...
        if (userId == 0) {
            managers.remove(userId)
        }

        if (!managers.containsKey(userId)) {
            managers[userId] = KitsuManager(
                KitsuApi(authHandler),
                userId
            )
        }

        return managers[userId]!!
    }
}