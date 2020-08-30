package com.chesire.nekome.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chesire.nekome.core.models.SeriesModel

/**
 * Provides a migration from version 1 to version 2 of the database.
 *
 * Change was the [SeriesModel.synopsis] was added.
 */
internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE seriesmodel ADD COLUMN synopsis TEXT NOT NULL DEFAULT ''")
    }
}
