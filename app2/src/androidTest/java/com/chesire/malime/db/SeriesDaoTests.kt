package com.chesire.malime.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
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
        assertTrue(seriesDao.get().isEmpty())
    }
}
