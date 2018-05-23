package com.chesire.malime.mal

class MalManagerFactory {
    fun get(auth: String, username: String): MalManager {
        return MalManager(auth, username)
    }
}