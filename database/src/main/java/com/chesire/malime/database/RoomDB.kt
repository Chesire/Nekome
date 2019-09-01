package com.chesire.malime.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.database.converters.ImageModelConverter
import com.chesire.malime.database.converters.SeriesStatusConverter
import com.chesire.malime.database.converters.SeriesTypeConverter
import com.chesire.malime.database.converters.ServiceConverter
import com.chesire.malime.database.converters.SubtypeConverter
import com.chesire.malime.database.converters.UserSeriesStatusConverter
import com.chesire.malime.database.dao.SeriesDao
import com.chesire.malime.database.dao.UserDao
import dagger.Reusable

@Reusable
@Database(
    entities = [SeriesModel::class, UserModel::class],
    version = 1
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
    abstract fun series(): SeriesDao
    abstract fun user(): UserDao
}
