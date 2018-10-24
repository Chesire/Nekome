package com.chesire.malime.core.repositories

import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.flags.SupportedService

class Authorization(private val authorizers: Map<SupportedService, Authorizer<*>>) {
    fun hasLoggedIn() = authorizers.any { !it.value.isDefaultUser(it.value.retrieveUser()) }

    @Suppress("UNCHECKED_CAST")
    fun <T> getUser(service: SupportedService): T? = authorizers[service]?.retrieveUser() as? T

    fun logoutAll() {
        SupportedService.values().forEach {
            authorizers[it]?.clear()
        }
    }
}
