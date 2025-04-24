package kolomyichuk.runly.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kolomyichuk.runly.data.local.room.converters.LatLngPointsConverter
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.Run

@Database(entities = [Run::class], version = 1)
@TypeConverters(LatLngPointsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRunDao():RunDao
}