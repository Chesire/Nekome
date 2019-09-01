package com.chesire.malime.repo

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.UserApi
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.db.UserDao
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi
) {
    val user = userDao.observe(Service.Kitsu)

    suspend fun refreshUser(): Resource<UserModel> {
        val response = userApi.getUserDetails()
        when (response) {
            is Resource.Success -> userDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing user")
        }

        return response
    }

    suspend fun retrieveUserId() = userDao.retrieveUserId(Service.Kitsu)
}
