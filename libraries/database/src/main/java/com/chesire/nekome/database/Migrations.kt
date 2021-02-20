@file:Suppress("LongMethod", "MaxLineLength")

package com.chesire.nekome.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Provides a migration from version 1 to version 2 of the database.
 *
 * Changes:
 * * synopsis was added to the seriesmodel table
 */
internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE seriesmodel ADD COLUMN synopsis TEXT NOT NULL DEFAULT ''")
    }
}

/**
 * Provides a migration from version 2 to version 3 of the database.
 *
 * Changes:
 * * SeriesEntity table created
 * * seriesmodel table data copied over to SeriesEntity table
 * * seriesmodel table dropped
 * * UserEntity table created
 * * userentity table data copied over to UserEntity table
 * * userentity table dropped
 */
internal val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        migrateSeries(database)
        migrateUser(database)
    }

    private fun migrateSeries(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE SeriesEntity (
                    id INTEGER PRIMARY KEY NOT NULL,
                    userId INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    subtype TEXT NOT NULL,
                    slug TEXT NOT NULL,
                    title TEXT NOT NULL,
                    seriesStatus TEXT NOT NULL,
                    userSeriesStatus TEXT NOT NULL,
                    progress INTEGER NOT NULL,
                    totalLength INTEGER NOT NULL,
                    posterImage TEXT NOT NULL,
                    startDate TEXT NOT NULL,
                    endDate TEXT NOT NULL
                )
            """.trimIndent()
        )
        database.execSQL(
            """
                INSERT INTO SeriesEntity (id, userId, type, subtype, slug, title, seriesStatus, userSeriesStatus, progress, totalLength, posterImage, startDate, endDate)
                SELECT id, userId, type, subtype, slug, title, seriesStatus, userSeriesStatus, progress, totalLength, posterImage, startDate, endDate FROM seriesmodel
            """.trimIndent()
        )
        database.execSQL(
            """
                DROP TABLE seriesmodel
            """.trimIndent()
        )
    }

    private fun migrateUser(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE UserEntity (
                    userId INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    avatar TEXT NOT NULL,
                    coverImage TEXT NOT NULL,
                    service TEXT PRIMARY KEY NOT NULL
                )
            """.trimIndent()
        )
        database.execSQL(
            """
                INSERT INTO UserEntity (userId, name, avatar, coverImage, service)
                SELECT userId, name, avatar, coverImage, service FROM usermodel
            """.trimIndent()
        )
        database.execSQL(
            """
                DROP TABLE usermodel
            """.trimIndent()
        )
    }
}

/**
 * Provides a migration from version 3 to version 4 of the database.
 *
 * Changes:
 * * rating was added to the SeriesEntity table
 */
internal val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE SeriesEntity ADD COLUMN rating INTEGER NOT NULL DEFAULT 0")
    }
}
