package com.chesire.nekome.auth.api

interface AuthEntityMapper<T> {
    fun mapFromAuthEntity(entity: AuthEntity): T
    fun mapToAuthEntity(item: T): AuthEntity
}
