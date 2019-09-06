package com.chesire.malime.account

import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.database.dao.UserDao
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.UserApi
import timber.log.Timber

/**
 * Repository to interact with user data.
 */
class UserRepository(
    private val userDao: UserDao,
    private val userApi: UserApi
) {
    /**
     * Access the user information.
     */
    val user = userDao.observe(Service.Kitsu)

    /**
     * Updates the stored user in the database, data will be funneled to the [user].
     */
    suspend fun refreshUser(): Resource<UserModel> {
        val response = userApi.getUserDetails()
        when (response) {
            is Resource.Success -> userDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing user")
        }

        return response
    }

    /**
     * Retrieves the user id from the database.
     */
    suspend fun retrieveUserId() = userDao.retrieveUserId(Service.Kitsu)
}
