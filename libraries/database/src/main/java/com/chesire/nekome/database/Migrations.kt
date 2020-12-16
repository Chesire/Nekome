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
 */
internal val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
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
}
