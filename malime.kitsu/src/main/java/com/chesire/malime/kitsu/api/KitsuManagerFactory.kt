package com.chesire.malime.kitsu.api

class KitsuManagerFactory {
    private val managers = HashMap<Int, KitsuManager>()

    fun get(accessToken: String = "", userId: Int = 0): KitsuManager {
        // remove the manager at user id 0, as its the default used to login
        // sorry person with userId of 0...
        if (userId == 0) {
            managers.remove(userId)
        }

        if (!managers.containsKey(userId)) {
            managers[userId] = KitsuManager(
                KitsuApi(accessToken),
                userId
            )
        }

        return managers[userId]!!
    }
}