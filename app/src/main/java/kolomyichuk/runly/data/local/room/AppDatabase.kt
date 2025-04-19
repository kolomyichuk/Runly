package kolomyichuk.runly.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.room.entity.Run

@Database(entities = [Run::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
}