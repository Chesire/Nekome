package com.chesire.malime.kitsu.api

class KitsuManagerFactory {
    private val managers = HashMap<Int, KitsuManager>()

    fun get(accessToken: String = "", userId: Int = 0): KitsuManager {
        if (!managers.containsKey(userId)) {
            // remove the manager at user id 0, as its the default
            if (userId == 0) {
                managers.remove(userId)
            }

            managers[userId] = KitsuManager(
                KitsuApi(accessToken),
                userId
            )
        }

        return managers[userId]!!
    }
}