package com.chesire.malime.mal.api

class MalManagerFactory {
    fun get(authorizer: MalAuthorizer): MalManager {
        return MalManager(
            MalApi(authorizer),
            ""
        )
        /*return MalManager(
            MalApi(authHandler),
            username
        )*/
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