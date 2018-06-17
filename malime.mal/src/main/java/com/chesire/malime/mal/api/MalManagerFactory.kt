package com.chesire.malime.mal.api

import com.chesire.malime.core.models.AuthModel

class MalManagerFactory {
    fun get(authModel: AuthModel = AuthModel("", "", 0), username: String = ""): MalManager {
        return MalManager(
            MalApi(authModel),
            username
        )
    }

    /*
    private val managers = HashMap<String, MalManager>()

    // Should test this to make sure there's no side effects with Retrofit
    fun get(auth: String = "", username: String = ""): MalManager {
        if (!managers.containsKey(username)) {
            managers[username] = MalManager(
                MalApi(auth),
                username
            )
        }

        return managers[username]!!
    }
    */
}