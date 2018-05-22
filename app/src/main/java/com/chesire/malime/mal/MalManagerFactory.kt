package com.chesire.malime.mal

class MalManagerFactory {
    fun get(auth: String): MalManager {
        return MalManager(auth)
    }
}