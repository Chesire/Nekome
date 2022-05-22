package com.chesire.nekome.datasource.user

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.database.dao.UserDao
import com.chesire.nekome.datasource.user.remote.UserApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Repository to interact with user data.
 */
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val map: UserMapper
) {

    /**
     * Retrieve the information about a user.
     */
    val user: Flow<User>
        get() = userDao.getUser(Service.Kitsu)
            .map { if (it == null) User.NotFound else User.Found(map.toUserDomain(it)) }
            .catch { User.NotFound }

    /**
     * Updates the stored user in the database, data will be funneled to the [user].
     */
    suspend fun refreshUser(): Resource<Unit> {
        return when (val response = userApi.getUserDetails()) {
            is Resource.Success -> {
                val model = map.toUserEntity(response.data)
                userDao.insert(model)
                Resource.Success(Unit)
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
}
