package com.chesire.malime.core.repositories

import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.flags.SupportedService

class Authorization(private val authorizers: Map<SupportedService, Authorizer<*>>) {
    fun hasLoggedIn(): Boolean {
        return authorizers.any {
            !it.value.isDefaultUser(it.value.retrieveUser())
        }
    }

    fun <T> getUser(service: SupportedService): T {
        return authorizers[service]?.retrieveUser() as T
    }

    fun logout(service: SupportedService) {
        authorizers[service]?.clear()
    }

    fun logoutAll() {
        authorizers.forEach { _, value ->
            value.clear()
        }
    }
}