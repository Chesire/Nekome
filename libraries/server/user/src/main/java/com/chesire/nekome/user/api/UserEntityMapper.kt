package com.chesire.nekome.user.api

interface UserEntityMapper<T> {
    fun mapFromUserEntity(entity: UserEntity): T
    fun mapToUserEntity(item: T): UserEntity
}
