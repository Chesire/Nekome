package com.chesire.malime.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.db.converters.ImageModelConverter
import com.chesire.malime.db.converters.SeriesStatusConverter
import com.chesire.malime.db.converters.SeriesTypeConverter
import com.chesire.malime.db.converters.ServiceConverter
import com.chesire.malime.db.converters.SubtypeConverter
import com.chesire.malime.db.converters.UserSeriesStatusConverter
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
