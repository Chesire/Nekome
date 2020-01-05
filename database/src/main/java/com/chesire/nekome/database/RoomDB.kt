package com.chesire.nekome.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.database.converters.ImageModelConverter
import com.chesire.nekome.database.converters.SeriesStatusConverter
import com.chesire.nekome.database.converters.SeriesTypeConverter
import com.chesire.nekome.database.converters.ServiceConverter
import com.chesire.nekome.database.converters.SubtypeConverter
import com.chesire.nekome.database.converters.UserSeriesStatusConverter
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.database.dao.UserDao

/**
 * Database for usage throughout the application, contains daos for interacting with the data.
 */
@Database(
    entities = [SeriesModel::class, UserModel::class],
    version = 2
)
@TypeConverters(
    ImageModelConverter::class,
    SeriesStatusConverter::class,
    SeriesTypeConverter::class,
    ServiceConverter::class,
    SubtypeConverter::class,
    UserSeriesStatusConverter::class
)
abstract class RoomDB : RoomDatabase() {
    /**
     * Dao for interacting with the Series data.
     */
    abstract fun series(): SeriesDao

    /**
     * Dao for interacting with the User data.
     */
    abstract fun user(): UserDao

    companion object {
        /**
         * Builds the database for usage.
         */
        fun build(context: Context, databaseName: String = "nekome_database.db"): RoomDB {
            return Room
                .databaseBuilder(context, RoomDB::class.java, databaseName)
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
        }

        /**
         * Builds a memory database for usage.
         */
        fun buildMemory(context: Context): RoomDB {
            return Room
                .inMemoryDatabaseBuilder(context, RoomDB::class.java)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
