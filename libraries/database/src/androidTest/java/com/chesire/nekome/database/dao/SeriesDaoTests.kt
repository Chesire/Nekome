package com.chesire.nekome.database.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.database.entity.SeriesEntity
import com.chesire.nekome.dataflags.SeriesStatus
import com.chesire.nekome.dataflags.SeriesType
import com.chesire.nekome.dataflags.Subtype
import com.chesire.nekome.dataflags.UserSeriesStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SeriesDaoTests {
    private lateinit var db: RoomDB
    private lateinit var seriesDao: SeriesDao

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
                seriesDao = it.series()
            }
    }

    @After
    fun teardown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun noSeriesReturnsEmptyList() = runBlocking {
        assertTrue(seriesDao.retrieve().isEmpty())
    }

    @Test
    fun deleteWithModelRemovesFromDatabase() = runBlocking {
        val model = createSeriesEntity()
        seriesDao.insert(model)
        assertTrue(seriesDao.retrieve().contains(model))
        seriesDao.delete(model)
        assertTrue(seriesDao.retrieve().isEmpty())
    }

    @Test
    fun insertWithSingleModelReplacesMatchingItem() = runBlocking {
        val model = createSeriesEntity()
        seriesDao.insert(model)
        assertTrue(seriesDao.retrieve().count() == 1)
        seriesDao.insert(model)
        assertTrue(seriesDao.retrieve().count() == 1)
    }

    @Test
    fun insertWithListOfItemsAddsNewItems() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesEntity(id = 0),
                createSeriesEntity(id = 1),
                createSeriesEntity(id = 2)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 3)
    }

    @Test
    fun insertWithListOfItemsReplacesMatchingItems() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesEntity(id = 0, userId = 0),
                createSeriesEntity(id = 1),
                createSeriesEntity(id = 2)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 3)
        seriesDao.insert(
            listOf(
                createSeriesEntity(id = 0, userId = 1),
                createSeriesEntity(id = 1),
                createSeriesEntity(id = 2)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 3)
        assertTrue(seriesDao.retrieve().first().userId == 1)
    }

    @Test
    fun retrieveGetsAllCurrentItems() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesEntity(id = 0),
                createSeriesEntity(id = 1),
                createSeriesEntity(id = 2)
            )
        )
        seriesDao.insert(
            listOf(
                createSeriesEntity(id = 3),
                createSeriesEntity(id = 4),
                createSeriesEntity(id = 5)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 6)
    }

    @Test
    fun retrieveGetsAllCurrentItemsBasedOnType() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesEntity(id = 0, type = SeriesType.Anime),
                createSeriesEntity(id = 1, type = SeriesType.Manga),
                createSeriesEntity(id = 2, type = SeriesType.Anime),
                createSeriesEntity(id = 3, type = SeriesType.Manga),
                createSeriesEntity(id = 4, type = SeriesType.Anime),
                createSeriesEntity(id = 5, type = SeriesType.Manga)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 6)
        assertTrue(seriesDao.retrieve().filter { it.type == SeriesType.Anime }.count() == 3)
    }

    @Test
    fun updateUpdatesAnyMatchingModel() = runBlocking {
        seriesDao.insert(createSeriesEntity(id = 0, userId = 0))
        assertTrue(seriesDao.retrieve().count() == 1)
        seriesDao.update(createSeriesEntity(id = 0, userId = 1))
        assertTrue(seriesDao.retrieve().first().userId == 1)
    }

    private fun createSeriesEntity(
        id: Int = 0,
        userId: Int = 0,
        type: SeriesType = SeriesType.Unknown
    ) = SeriesEntity(
        id,
        userId,
        type,
        Subtype.Unknown,
        "slug",
        "title",
        SeriesStatus.Unknown,
        UserSeriesStatus.Unknown,
        0,
        0,
        ImageModel.empty,
        "",
        ""
    )
}
