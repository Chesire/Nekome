package com.chesire.nekome.helpers

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.testing.createUserEntity
import kotlinx.coroutines.runBlocking

/**
 * Creates and pushes a new user into the [UserDao].
 */
fun UserDao.createTestUser(
    userId: Int = 0,
    name: String = "Nekome",
    avatar: ImageModel = ImageModel.empty,
    coverImage: ImageModel = ImageModel.empty
) = runBlocking {
    insert(createUserEntity(userId, name, avatar, coverImage))
}
