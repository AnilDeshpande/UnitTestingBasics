package com.codetutor.unittestingbasics.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codetutor.unittestingbasics.model.Country

@Database(
    entities = [Country::class],
    version = 1,
    exportSchema = false  // Setting to false to avoid schema export warning
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryRoomDao
}
