package com.chesire.nekome.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import java.io.IOException
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.Is
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MigrationTests {
    private val testDB = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        RoomDB::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    private val allMigrations = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4,
        MIGRATION_4_5
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(testDB, 1)

        // db has schema version 1. insert some data using SQL queries.
        val userValues = ContentValues().apply {
            put("userId", 7)
            put("name", "Nekome")
            put("avatar", "avatarData")
            put("coverImage", "coverImageData")
            put("service", "Kitsu")
        }
        val seriesValues = ContentValues().apply {
            put("id", 10)
            put("userId", 20)
            put("type", "typeData")
            put("subtype", "subtypeData")
            put("slug", "slugData")
            put("title", "titleData")
            put("seriesStatus", "seriesStatusData")
            put("userSeriesStatus", "userSeriesStatusData")
            put("progress", 30)
            put("totalLength", 40)
            put("posterImage", "posterImageData")
            put("coverImage", "coverImageData")
            put("nsfw", 1)
            put("startDate", "startDateData")
            put("endDate", "endDateData")
        }

        val userId = db.insert("usermodel", SQLiteDatabase.CONFLICT_REPLACE, userValues)
        val seriesId = db.insert("seriesmodel", SQLiteDatabase.CONFLICT_REPLACE, seriesValues)
        db.close()

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(testDB, 2, true, MIGRATION_1_2)

        // Ensure that the data is still valid.
        with(db.query("SELECT * FROM usermodel WHERE rowid = ?", arrayOf(userId))) {
            assertThat(this, Is(notNullValue()))
            moveToFirst()

            assertEquals(7, getInt(0))
            assertEquals("Nekome", getString(1))
            assertEquals("avatarData", getString(2))
            assertEquals("coverImageData", getString(3))
            assertEquals("Kitsu", getString(4))
        }
        with(db.query("SELECT * FROM seriesmodel WHERE rowid = ?", arrayOf(seriesId))) {
            assertThat(this, Is(notNullValue()))
            moveToFirst()

            assertEquals(10, getInt(0))
            assertEquals(20, getInt(1))
            assertEquals("typeData", getString(2))
            assertEquals("subtypeData", getString(3))
            assertEquals("slugData", getString(4))
            assertEquals("titleData", getString(5))
            assertEquals("seriesStatusData", getString(6))
            assertEquals("userSeriesStatusData", getString(7))
            assertEquals(30, getInt(8))
            assertEquals(40, getInt(9))
            assertEquals("posterImageData", getString(10))
            assertEquals("coverImageData", getString(11))
            assertEquals(1, getInt(12))
            assertEquals("startDateData", getString(13))
            assertEquals("endDateData", getString(14))
            // Synopsis is now added
            assertEquals("", getString(15))
            assertEquals("synopsis", getColumnName(15))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        var db = helper.createDatabase(testDB, 2)

        // db has schema version 2. insert some data using SQL queries.
        val userValues = ContentValues().apply {
            put("userId", 7)
            put("name", "Nekome")
            put("avatar", "avatarData")
            put("coverImage", "coverImageData")
            put("service", "Kitsu")
        }
        val seriesValues = ContentValues().apply {
            put("id", 10)
            put("userId", 20)
            put("type", "typeData")
            put("subtype", "subtypeData")
            put("slug", "slugData")
            put("synopsis", "synopsisData")
            put("title", "titleData")
            put("seriesStatus", "seriesStatusData")
            put("userSeriesStatus", "userSeriesStatusData")
            put("progress", 30)
            put("totalLength", 40)
            put("posterImage", "posterImageData")
            put("coverImage", "coverImageData")
            put("nsfw", 1)
            put("startDate", "startDateData")
            put("endDate", "endDateData")
        }

        val userId = db.insert("usermodel", SQLiteDatabase.CONFLICT_REPLACE, userValues)
        val seriesId = db.insert("seriesmodel", SQLiteDatabase.CONFLICT_REPLACE, seriesValues)
        db.close()

        // Re-open the database with version 3 and provide
        // MIGRATION_2_3 as the migration process.
        db = helper.runMigrationsAndValidate(testDB, 3, true, MIGRATION_2_3)

        // Ensure that the data is still valid.
        with(db.query("SELECT * FROM UserEntity WHERE rowid = ?", arrayOf(userId))) {
            assertThat(this, Is(notNullValue()))
            moveToFirst()

            assertEquals(7, getInt(0))
            assertEquals("Nekome", getString(1))
            assertEquals("avatarData", getString(2))
            assertEquals("coverImageData", getString(3))
            assertEquals("Kitsu", getString(4))
        }
        with(db.query("SELECT * FROM SeriesEntity WHERE rowid = ?", arrayOf(seriesId))) {
            assertThat(this, Is(notNullValue()))
            moveToFirst()

            assertEquals(10, getInt(0))
            assertEquals(20, getInt(1))
            assertEquals("typeData", getString(2))
            assertEquals("subtypeData", getString(3))
            assertEquals("slugData", getString(4))
            assertEquals("titleData", getString(5))
            assertEquals("seriesStatusData", getString(6))
            assertEquals("userSeriesStatusData", getString(7))
            assertEquals(30, getInt(8))
            assertEquals(40, getInt(9))
            assertEquals("posterImageData", getString(10))
            assertEquals("startDateData", getString(11))
            assertEquals("endDateData", getString(12))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate3to4() {
        var db = helper.createDatabase(testDB, 3)

        // db has schema version 3. insert some data using SQL queries.
        val userValues = ContentValues().apply {
            put("userId", 7)
            put("name", "Nekome")
            put("avatar", "avatarData")
            put("coverImage", "coverImageData")
            put("service", "Kitsu")
        }
        val seriesValues = ContentValues().apply {
            put("id", 10)
            put("userId", 20)
            put("type", "typeData")
            put("subtype", "subtypeData")
            put("slug", "slugData")
            put("title", "titleData")
            put("seriesStatus", "seriesStatusData")
            put("userSeriesStatus", "userSeriesStatusData")
            put("progress", 30)
            put("totalLength", 40)
            put("posterImage", "posterImageData")
            put("startDate", "startDateData")
            put("endDate", "endDateData")
        }

        val userId = db.insert("UserEntity", SQLiteDatabase.CONFLICT_REPLACE, userValues)
        val seriesId = db.insert("SeriesEntity", SQLiteDatabase.CONFLICT_REPLACE, seriesValues)
        db.close()

        // Re-open the database with version 4 and provide
        // MIGRATION_3_4 as the migration process.
        db = helper.runMigrationsAndValidate(testDB, 4, true, MIGRATION_3_4)

        // Ensure that the data is still valid.
        with(db.query("SELECT * FROM UserEntity WHERE rowid = ?", arrayOf(userId))) {
            assertThat(this, Is(notNullValue()))
            moveToFirst()

            assertEquals(7, getInt(0))
            assertEquals("Nekome", getString(1))
            assertEquals("avatarData", getString(2))
            assertEquals("coverImageData", getString(3))
            assertEquals("Kitsu", getString(4))
        }
        with(db.query("SELECT * FROM SeriesEntity WHERE rowid = ?", arrayOf(seriesId))) {
            assertThat(this, Is(notNullValue()))
            moveToFirst()

            assertEquals(10, getInt(0))
            assertEquals(20, getInt(1))
            assertEquals("typeData", getString(2))
            assertEquals("subtypeData", getString(3))
            assertEquals("slugData", getString(4))
            assertEquals("titleData", getString(5))
            assertEquals("seriesStatusData", getString(6))
            assertEquals("userSeriesStatusData", getString(7))
            assertEquals(30, getInt(8))
            assertEquals(40, getInt(9))
            assertEquals("posterImageData", getString(10))
            assertEquals("startDateData", getString(11))
            assertEquals("endDateData", getString(12))
            assertEquals(0, getInt(13))
            assertEquals("rating", getColumnName(13))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        // Create earliest version of the database.
        helper.createDatabase(testDB, 1).apply {
            close()
        }

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            RoomDB::class.java,
            testDB
        ).addMigrations(*allMigrations).build().apply {
            openHelper.writableDatabase
            close()
        }
    }
}
