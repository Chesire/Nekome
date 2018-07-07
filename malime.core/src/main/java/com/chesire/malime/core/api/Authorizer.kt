package com.chesire.malime.core.api

import com.chesire.malime.core.models.AuthModel

interface Authorizer<T> {
    fun storeAuthDetails(model: AuthModel)
    fun retrieveAuthDetails(): AuthModel
    fun storeUser(user: T)
    fun retrieveUser(): T
    fun clear()
}