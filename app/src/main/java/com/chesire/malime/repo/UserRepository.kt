package com.chesire.malime.repo

import com.chesire.malime.core.api.UserApi
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.db.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi
) {
    suspend fun getUser() = userApi.getUserDetails()
    suspend fun insertUser(user: UserModel) = userDao.insert(user)
}
