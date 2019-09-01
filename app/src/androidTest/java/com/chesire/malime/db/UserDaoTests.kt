package com.chesire.malime.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.chesire.malime.server.flags.Service
import com.chesire.malime.server.models.ImageModel
import com.chesire.malime.server.models.UserModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTests {
    private lateinit var db: RoomDB
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        db = Room
            .inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                RoomDB::class.java
            )
            .fallbackToDestructiveMigration()
            .build()
            .also {
                userDao = it.user()
            }
    }

    @After
    fun teardown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun noUserDetailsReturnsNull() = runBlocking {
        assertNull(userDao.retrieve(Service.Kitsu))
    }

    @Test
    fun canRetrieveKitsuUser() = runBlocking {
        userDao.insert(createUserModel(service = Service.Kitsu))
        assertNotNull(userDao.retrieve(Service.Kitsu))
    }

    @Test
    fun retrievedImageModelIsCorrect() = runBlocking {
        val expected = ImageModel(
            ImageModel.ImageData("tinyUrl", 0, 1),
            ImageModel.ImageData("smallUrl", 2, 3),
            ImageModel.ImageData("mediumUrl", 4, 5),
            ImageModel.ImageData("largeUrl", 6, 7)
        )
        userDao.insert(createUserModel(avatar = expected, service = Service.Kitsu))

        assertEquals(expected, userDao.retrieve(Service.Kitsu).avatar)
    }

    @Test
    fun duplicateCallsReplaceUser() = runBlocking {
        val first = createUserModel(id = 1, name = "Model1", service = Service.Kitsu)
        val second = createUserModel(id = 2, name = "Model2", service = Service.Kitsu)

        userDao.insert(first)
        assertEquals(first, userDao.retrieve(Service.Kitsu))
        userDao.insert(second)
        assertEquals(second, userDao.retrieve(Service.Kitsu))
    }

    @Test
    fun canStoreMultipleUsersBasedOnService() = runBlocking {
        val first = createUserModel(id = 0, service = Service.Kitsu)
        val second = createUserModel(id = 0, service = Service.Unknown)

        userDao.insert(first)
        userDao.insert(second)
        assertEquals(first, userDao.retrieve(Service.Kitsu))
        assertEquals(second, userDao.retrieve(Service.Unknown))
    }

    @Test
    fun deletingUsingUserModelRemoves() = runBlocking {
        val model = createUserModel(service = Service.Kitsu)
        userDao.insert(model)
        assertEquals(model, userDao.retrieve(Service.Kitsu))
        userDao.delete(model)
        assertNull(userDao.retrieve(Service.Kitsu))
    }

    @Test
    fun deletingUsingServiceRemoves() = runBlocking {
        val model = createUserModel(service = Service.Kitsu)
        userDao.insert(model)
        assertEquals(model, userDao.retrieve(Service.Kitsu))
        userDao.delete(Service.Kitsu)
        assertNull(userDao.retrieve(Service.Kitsu))
    }

    @Test
    fun retrievingUserIdGetsNullIfNotExist() = runBlocking {
        assertNull(userDao.retrieveUserId(Service.Kitsu))
    }

    @Test
    fun retrievingUserIdGetsIdIfExists() = runBlocking {
        val model = createUserModel(id = 133, service = Service.Kitsu)
        userDao.insert(model)
        assertEquals(133, userDao.retrieveUserId(Service.Kitsu))
    }

    private fun createUserModel(
        id: Int = 0,
        name: String = "test",
        avatar: ImageModel = ImageModel.empty,
        cover: ImageModel = ImageModel.empty,
        service: Service = Service.Unknown
    ) = UserModel(id, name, avatar, cover, service)
}
