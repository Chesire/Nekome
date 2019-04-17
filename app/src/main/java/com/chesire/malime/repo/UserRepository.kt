package com.chesire.malime.repo

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.db.UserDao
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi
) {
    suspend fun refreshUser(): Resource<UserModel> {
        val response = userApi.getUserDetails()
        when (response) {
            is Resource.Success -> userDao.insert(response.data)
            is Resource.Error -> Timber.e("Error refreshing user")
        }

        return response
    }

    suspend fun retrieveUser() = userDao.retrieve(Service.Kitsu)
    suspend fun retrieveUserId() = userDao.retrieveUserId(Service.Kitsu)
}
