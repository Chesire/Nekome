package com.chesire.malime.mal

import com.chesire.malime.mal.api.MalManager

class MalManagerFactory {
    fun get(auth: String, username: String): MalManager {
        return MalManager(auth, username)
    }
}