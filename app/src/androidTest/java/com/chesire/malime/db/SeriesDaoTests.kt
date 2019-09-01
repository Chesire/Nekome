package com.chesire.malime.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.chesire.malime.server.flags.SeriesStatus
import com.chesire.malime.server.flags.SeriesType
import com.chesire.malime.server.flags.Subtype
import com.chesire.malime.server.flags.UserSeriesStatus
import com.chesire.malime.server.models.ImageModel
import com.chesire.malime.server.models.SeriesModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SeriesDaoTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

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
        val model = createSeriesModel()
        seriesDao.insert(model)
        assertTrue(seriesDao.retrieve().contains(model))
        seriesDao.delete(model)
        assertTrue(seriesDao.retrieve().isEmpty())
    }

    @Test
    fun insertWithSingleModelReplacesMatchingItem() = runBlocking {
        val model = createSeriesModel()
        seriesDao.insert(model)
        assertTrue(seriesDao.retrieve().count() == 1)
        seriesDao.insert(model)
        assertTrue(seriesDao.retrieve().count() == 1)
    }

    @Test
    fun insertWithListOfItemsAddsNewItems() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesModel(id = 0),
                createSeriesModel(id = 1),
                createSeriesModel(id = 2)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 3)
    }

    @Test
    fun insertWithListOfItemsReplacesMatchingItems() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesModel(id = 0, userId = 0),
                createSeriesModel(id = 1),
                createSeriesModel(id = 2)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 3)
        seriesDao.insert(
            listOf(
                createSeriesModel(id = 0, userId = 1),
                createSeriesModel(id = 1),
                createSeriesModel(id = 2)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 3)
        assertTrue(seriesDao.retrieve().first().userId == 1)
    }

    @Test
    fun observeNotifiesOnDaoUpdating() = runBlocking {
        val mockObserver = mockk<Observer<List<SeriesModel>>> {
            every { onChanged(any()) } just Runs
        }

        seriesDao.observe().observeForever(mockObserver)

        val expected = createSeriesModel(id = 0)
        seriesDao.insert(expected)

        verify { mockObserver.onChanged(listOf(expected)) }
    }

    @Test
    fun observeOnTypeNotifiesUpdating() = runBlocking {
        val mockObserver = mockk<Observer<List<SeriesModel>>> {
            every { onChanged(any()) } just Runs
        }

        seriesDao.observe(SeriesType.Manga).observeForever(mockObserver)

        val expected = listOf(
            createSeriesModel(id = 0, type = SeriesType.Manga),
            createSeriesModel(id = 1, type = SeriesType.Manga),
            createSeriesModel(id = 2, type = SeriesType.Manga)
        )
        val notExpected = listOf(
            createSeriesModel(id = 3, type = SeriesType.Anime),
            createSeriesModel(id = 4, type = SeriesType.Anime),
            createSeriesModel(id = 5, type = SeriesType.Anime)
        )
        val insert = expected + notExpected
        seriesDao.insert(insert)

        verify { mockObserver.onChanged(expected) }
        verifyAll(inverse = true) {
            mockObserver.onChanged(notExpected)
            mockObserver.onChanged(insert)
        }
    }

    @Test
    fun retrieveGetsAllCurrentItems() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesModel(id = 0),
                createSeriesModel(id = 1),
                createSeriesModel(id = 2)
            )
        )
        seriesDao.insert(
            listOf(
                createSeriesModel(id = 3),
                createSeriesModel(id = 4),
                createSeriesModel(id = 5)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 6)
    }

    @Test
    fun retrieveGetsAllCurrentItemsBasedOnType() = runBlocking {
        seriesDao.insert(
            listOf(
                createSeriesModel(id = 0, type = SeriesType.Anime),
                createSeriesModel(id = 1, type = SeriesType.Manga),
                createSeriesModel(id = 2, type = SeriesType.Anime),
                createSeriesModel(id = 3, type = SeriesType.Manga),
                createSeriesModel(id = 4, type = SeriesType.Anime),
                createSeriesModel(id = 5, type = SeriesType.Manga)
            )
        )
        assertTrue(seriesDao.retrieve().count() == 6)
        assertTrue(seriesDao.retrieve(SeriesType.Anime).count() == 3)
    }

    @Test
    fun updateUpdatesAnyMatchingModel() = runBlocking {
        seriesDao.insert(createSeriesModel(id = 0, userId = 0))
        assertTrue(seriesDao.retrieve().count() == 1)
        seriesDao.update(createSeriesModel(id = 0, userId = 1))
        assertTrue(seriesDao.retrieve().first().userId == 1)
    }

    private fun createSeriesModel(
        id: Int = 0,
        userId: Int = 0,
        type: SeriesType = SeriesType.Unknown
    ) = SeriesModel(
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
        ImageModel.empty,
        false,
        "",
        ""
    )
}
