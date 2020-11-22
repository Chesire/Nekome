package com.chesire.nekome.account

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.user.api.UserApi
import com.chesire.nekome.user.api.UserEntity
import timber.log.Timber
import javax.inject.Inject

/**
 * Repository to interact with user data.
 */
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi
) {
    /**
     * Access the user information.
     */
    val user = userDao.getUser(Service.Kitsu)

    /**
     * Updates the stored user in the database, data will be funneled to the [user].
     */
    suspend fun refreshUser(): Resource<UserModel> {
        val response = userApi.getUserDetails()
        return when (response) {
            is Resource.Success -> {
                val model = response.data.toUserModel()
                userDao.insert(model)
                Resource.Success(model)
            }
            is Resource.Error -> {
                Timber.e("Error refreshing user")
                Resource.Error(response.msg)
            }
        }
    }

    /**
     * Retrieves the user id from the database.
     */
    suspend fun retrieveUserId() = userDao.retrieveUserId(Service.Kitsu)

    private fun UserEntity.toUserModel(): UserModel {
        return UserModel(
            userId,
            name,
            avatar,
            coverImage,
            service
        )
    }
}
