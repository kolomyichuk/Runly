package kolomyichuk.runly.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kolomyichuk.runly.data.local.room.converters.LocationConverter
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.Run

@Database(entities = [Run::class], version = 2)
@TypeConverters(LocationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRunDao(): RunDao
}

