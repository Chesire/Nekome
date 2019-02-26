package com.chesire.malime.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.db.converters.FlagConverters
import com.chesire.malime.db.converters.ImageModelConverter
import dagger.Reusable

@Reusable
@Database(
    entities = [UserModel::class],
    version = 1
)
@TypeConverters(FlagConverters::class, ImageModelConverter::class)
abstract class RoomDB : RoomDatabase() {
    abstract fun user(): UserDao
}
